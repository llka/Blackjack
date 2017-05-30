package ru.ilka.logic;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.comparator.BanComparator;
import ru.ilka.comparator.HandsWonComparator;
import ru.ilka.comparator.ProfitComparator;
import ru.ilka.comparator.RatingComparator;
import ru.ilka.dao.AccountDao;
import ru.ilka.entity.Account;
import ru.ilka.exception.DBException;
import ru.ilka.exception.LogicException;
import ru.ilka.validator.AccountValidator;

import java.math.BigDecimal;
import java.util.List;

/**
 * AccountLogic class provides functionality for Account class.
 * @see Account
 * @since %G%
 * @version %I%
 */
public class AccountLogic {
    static Logger logger = LogManager.getLogger(AccountLogic.class);

    public AccountLogic() {}

    /**
     * Checks if there is account in data base with this params.
     * @param loginOrEmail login or email
     * @param password password
     * @return <code>true</code> if loginOrEmail and password
     *         are correct appropriate account exists;
     *         <code>false</code> otherwise.
     * @throws LogicException If a DBException occurred.
     */
    public boolean checkLogIn(String loginOrEmail, String password) throws LogicException {
        AccountDao accountDao = new AccountDao();
        if (AccountValidator.validateAuthorization(loginOrEmail, password)) {
            try {
                String salt = accountDao.getBirthDateByAuthorization(loginOrEmail);
                password = encodeWithSHA512(password,salt);
            }catch (DBException e){
                return false;
            }
            try {
                return accountDao.authorizeUser(loginOrEmail, password);
            } catch (DBException e) {
                throw new LogicException("Error in account authorization " + e);
            }
        } else {
            return false;
        }
    }

    /**
     * Inserts new user in data base.
     * @param firstName visitor's name
     * @param lastName visitor's surname
     * @param email visitor's unique email address
     * @param login visitor's unique login
     * @param password visitor's password
     * @param birthDate visitor's real birth date
     * @param gender visitor's gender type
     * @param avatar path to avatar file
     * @param inviteCode invite code
     * @return <code>LogicResult.OK</code> if all params are correct;
     *         <code>LogicResult.EMAIL_UNUNIQUE</code> if this email was already used;
     *         <code>LogicResult.LOGIN_UNUNIQUE</code> if this login was already used;
     *         <code>LogicResult.REGISTER_INVITE_WRONG</code> if this invite code do not exists;
     *         <code>LogicResult.FAILED</code> otherwise.
     * @throws LogicException If a DBException occurred.
     */
    public LogicResult register(String firstName, String lastName, String email, String login, String password,
                                String birthDate, String gender, String avatar, String inviteCode) throws LogicException {
        AccountDao accountDao = new AccountDao();
        LogicResult logicResult;
        if(inviteCode == null){
            inviteCode = "1234-1234";
        }
        try {
            if (!AccountValidator.validateEmail(email)) {
                logger.debug("invalid email");
                return LogicResult.FAILED;
            }else if (!AccountValidator.validateLogin(login)) {
                logger.debug("invalid log");
                return LogicResult.FAILED;
            } else if (!AccountValidator.validatePassword(password)) {
                logger.debug("invalid pass");
                return LogicResult.FAILED;
            }else if (!accountDao.isEmailUnique(email)) {
                return LogicResult.EMAIL_UNUNIQUE;
            }else if(!accountDao.isLoginUnique(login)){
                return LogicResult.LOGIN_UNUNIQUE;
            }else if(!AccountValidator.validateInviteCode(inviteCode)){
                return LogicResult.REGISTER_INVITE_WRONG;
            }else if(!AccountValidator.validateGender(gender)){
                    logger.debug("invalid gender");
                    return LogicResult.FAILED;
            }else {
                password = encodeWithSHA512(password,birthDate);
                accountDao.register(firstName,lastName,email,login,password,birthDate,gender,avatar,inviteCode);
                logicResult = LogicResult.OK;
            }
        }catch (DBException e) {
            throw new LogicException("Error in logic while checking register params in database." + e);
        }
        return logicResult;
    }

    /**
     * Loads an account from data base using loginOrEmail.
     * @param loginOrEmail visitor's unique email or login
     * @return account
     * @throws LogicException If a DBException occurred.
     */
    public Account loadAccountByAuthorization(String loginOrEmail) throws LogicException {
        AccountDao accountDao = new AccountDao();
        try {
            Account account = accountDao.getAccountByAuthorization(loginOrEmail);
            return account;
        } catch (DBException e) {
            throw new LogicException("Error while loading account from database by login or email." + e);
        }
    }

    /**
     * Loads an account from data base using login.
     * @param login visitor's unique login
     * @return account
     * @throws LogicException If a DBException occurred.
     */
    public Account loadAccountByLogin(String login) throws LogicException {
        AccountDao accountDao = new AccountDao();
        try {
            Account account = accountDao.getAccountByLogin(login);
            return account;
        } catch (DBException e) {
            throw new LogicException("Error while loading account from database by login" + e);
        }
    }

    /**
     * Loads an account from data base using id.
     * @param id account id number
     * @return account
     * @throws LogicException If a DBException occurred.
     */
    public Account loadAccountById(int id) throws LogicException {
        AccountDao accountDao = new AccountDao();
        try {
            Account account = accountDao.getAccountById(id);
            return account;
        } catch (DBException e) {
            throw new LogicException("Error while loading account from database by id " + e);
        }
    }

    /**
     * Loads all accounts except account with this id from data base.
     * @param accountId admin id
     * @return accounts list
     * @throws LogicException If a DBException occurred.
     */
    public List<Account> loadAllAccounts(int accountId) throws LogicException {
        AccountDao accountDao = new AccountDao();
        try {
            List<Account> accounts = accountDao.loadAllAccounts(accountId);
            return accounts;
        } catch (DBException e) {
            throw new LogicException("Error while loading all accounts from database " + e);
        }
    }

    /**
     * Loads all accounts with admin role except account with this id
     * @param accountId admin id
     * @return account list
     * @throws LogicException If a DBException occurred.
     */
    public List<Account> loadAllAdmins(int accountId) throws LogicException {
        AccountDao accountDao = new AccountDao();
        try {
            List<Account> accounts = accountDao.loadAllAdmins(accountId);
            return accounts;
        } catch (DBException e) {
            throw new LogicException("Error while loading all admin accounts from database " + e);
        }
    }

    /**
     * Changes user's first name.
     * @param accountId user account id number
     * @param name new name
     * @throws LogicException If a DBException occurred.
     */
    public void changeFirstName(int accountId, String name) throws LogicException {
        AccountDao accountDao = new AccountDao();
        try {
            accountDao.updateFirstName(accountId,name);
        } catch (DBException e) {
            throw new LogicException("Error while updating firstName" + e);
        }
    }

    /**
     * Changes user's last name.
     * @param accountId user account id number
     * @param name new last name
     * @throws LogicException If a DBException occurred.
     */
    public void changeLastName(int accountId, String name) throws LogicException {
        AccountDao accountDao = new AccountDao();
        try {
            accountDao.updateLastName(accountId,name);
        } catch (DBException e) {
            throw new LogicException("Error while updating lastName" + e);
        }
    }

    /**
     * Changes user's email address.
     * @param accountId user account id number
     * @param email new email address
     * @return <code>INVALID_EMAIL</code> if new email has invalid symbols;
     *         <code>EMAIL_UNUNIQUE</code> if new email is already used;
     *         <code>OK</code> otherwise.
     * @throws LogicException If a DBException occurred.
     */
    public LogicResult changeEmail(int accountId, String email) throws LogicException {
        AccountDao accountDao = new AccountDao();
        try {
            if (!AccountValidator.validateEmail(email)) {
                return LogicResult.INVALID_EMAIL;
            } else if (!accountDao.isEmailUnique(email)) {
                return LogicResult.EMAIL_UNUNIQUE;
            } else {
                accountDao.updateEmail(accountId, email);
                return LogicResult.OK;
            }
        } catch (DBException e) {
            throw new LogicException("Error while updating email" + e);
        }
    }

    /**
     * Changes user's login.
     * @param accountId user account id number
     * @param login new unique login
     * @return <code>INVALID_LOGIN</code> if new login has invalid symbols;
     *         <code>LOGIN_UNUNIQUE</code> if new login is already used;
     *         <code>OK</code> otherwise.
     * @throws LogicException If a DBException occurred.
     */
    public LogicResult changeLogin(int accountId, String login) throws LogicException {
        AccountDao accountDao = new AccountDao();
        try {
            if (!AccountValidator.validateLogin(login)) {
                return LogicResult.INVALID_LOGIN;
            } else if (!accountDao.isLoginUnique(login)) {
                return LogicResult.LOGIN_UNUNIQUE;
            } else {
                accountDao.updateLogin(accountId, login);
                return LogicResult.OK;
            }
        } catch (DBException e) {
            throw new LogicException("Error while updating login" + e);
        }
    }

    /**
     * Changes user's password.
     * @param accountId user account id number
     * @param password new password
     * @return <code>INVALID_PASSWORD</code> if new password has invalid symbols;
     *         <code>OK</code> otherwise.
     * @throws LogicException If a DBException occurred.
     */
    public LogicResult changePassword(int accountId, String password) throws LogicException {
        AccountDao accountDao = new AccountDao();
        try {
            if (!AccountValidator.validatePassword(password)) {
                return LogicResult.INVALID_PASSWORD;
            }else {
                password = encodeWithSHA512(password, accountDao.getAccountById(accountId).getBirthDate().toString());
                accountDao.updatePassword(accountId, password);
                return LogicResult.OK;
            }
        } catch (DBException e) {
            throw new LogicException("Error while updating password" + e);
        }
    }

    /**
     * Verifies this pay card.
     * @param account user's account object
     * @param name name and surname as on the pay card
     * @param timeLeft month number
     * @return <code>WRONG_NAME</code> if name is wrong;
     *         <code>UNAVALIABLE_CARD</code> if month number is below one;
     *         <code>OK</code> otherwise.
     */
    public LogicResult checkPayCard(Account account, String name, int timeLeft){
        String realName = account.getFirstName() + " " + account.getLastName();
        if(!name.toUpperCase().equals(realName.toUpperCase())){
            return LogicResult.WRONG_NAME;
        }else if(timeLeft < 1){
            return LogicResult.UNAVALIABLE_CARD;
        }
        else{
            return LogicResult.OK;
        }
    }

    /**
     * Adds amount to user's balance.
     * @param accountId user account id number
     * @param amount money to add
     * @throws LogicException If a DBException occurred.
     */
    public void addToBalance(int accountId, BigDecimal amount) throws LogicException {
        AccountDao accountDao = new AccountDao();
        try {
            Account account = accountDao.getAccountById(accountId);
            accountDao.updateBalance(accountId, account.getBalance().add(amount));
        } catch (DBException e) {
            throw new LogicException("Error while adding some money to balance" + e);
        }
    }

    /**
     * Sets new balance value
     * @param accountId user account id number
     * @param balance new balance size
     * @throws LogicException If a DBException occurred.
     */
    public void changeBalance(int accountId, BigDecimal balance) throws LogicException{
        AccountDao accountDao = new AccountDao();
        try {
            accountDao.updateBalance(accountId, balance);
        } catch (DBException e) {
            throw new LogicException("Error while updating balance" + e);
        }
    }

    /**
     * Changes avatar image
     * @param accountId user account id number
     * @param img path to new image
     * @throws LogicException If a DBException occurred.
     */
    public void changeAvatar(int accountId, String img) throws LogicException {
        AccountDao accountDao = new AccountDao();
        try {
            accountDao.updateAvatar(accountId,img);
        } catch (DBException e) {
            throw new LogicException("Error while updating avatar" + e);
        }
    }

    /**
     * Sorts accounts by the amount of the lost money - money, that casino has won.
     * @param accounts list of accounts to sort
     */
    public void sortByProfit(List<Account> accounts){
        accounts.sort(new ProfitComparator().
                thenComparing(new HandsWonComparator().
                        thenComparing(new RatingComparator())));
    }

    /**
     * Sorts accounts by their rating increase
     * @param accounts list of accounts to sort
     */
    public void sortByRating(List<Account> accounts){
        accounts.sort(new RatingComparator().
                thenComparing(new ProfitComparator().
                        thenComparing(new HandsWonComparator())));
    }

    /**
     * Sorts accounts by hand won quantity increase
     * @param accounts list of accounts to sort
     */
    public void sortByHandsWon(List<Account> accounts){
        accounts.sort(new HandsWonComparator().
                thenComparing(new ProfitComparator().
                        thenComparing(new RatingComparator())));
    }

    /**
     * Sorts accounts so, that first accounts with ban status.
     * @param accounts list of accounts to sort
     */
    public void sortByBanStatus(List<Account> accounts){
        accounts.sort(new BanComparator().
                thenComparing(new ProfitComparator().
                        thenComparing(new RatingComparator())));
    }

    /**
     * Updates ban status of this user
     * @param accountId user account id number
     * @param isBanned new ban status
     * @throws LogicException If a DBException occurred.
     */
    public void banAccount(int accountId, boolean isBanned) throws LogicException{
        AccountDao accountDao = new AccountDao();
        try {
            accountDao.updateBanStatus(accountId,isBanned);
        } catch (DBException e) {
            throw new LogicException("Error while banning user " + e);
        }
    }

    /**
     * Bans every user, who wins more money than loose.
     * @param accounts list of all accounts
     * @throws LogicException If a DBException occurred.
     */
    public void globalBan(List<Account> accounts) throws LogicException {
        AccountDao accountDao = new AccountDao();
        for (Account account : accounts) {
            BigDecimal profit = account.getMoneySpend().subtract(account.getMoneyWon());
            if(profit.doubleValue() < 0){
                try {
                    accountDao.updateBanStatus(account.getAccountId(),true);
                    account.setBan(true);
                } catch (DBException e) {
                    throw new LogicException("Error while banning user " + e);
                }
            }
        }
    }

    /**
     * Updates admin status
     * @param accountId user account id number
     * @param isAdmin new admin status of this account
     * @throws LogicException If a DBException occurred.
     */
    public void setAdminRole(int accountId, boolean isAdmin) throws LogicException{
        AccountDao accountDao = new AccountDao();
        try {
            accountDao.updateAdminRole(accountId,isAdmin);
        } catch (DBException e) {
            throw new LogicException("Error while updating admin role " + e);
        }
    }

    /**
     * Updates user's statistics.
     * @param accountId user account id number
     * @param played number of games played
     * @param handsWon number of games won
     * @param moneySpend amount of money was loosed
     * @param moneyWon amount of money was won
     * @param rating new rating
     * @throws LogicException If a DBException occurred.
     */
    public void changeStatistics(int accountId, int played, int handsWon, BigDecimal moneySpend, BigDecimal moneyWon, int rating) throws LogicException {
        AccountDao accountDao = new AccountDao();
        try {
            accountDao.updateStatistics(accountId,played,handsWon,moneySpend,moneyWon,rating);
        } catch (DBException e) {
            throw new LogicException("Error while updating statistics " + e);
        }
    }

    /**
     * Loads user's account id number.
     * @param login user's login
     * @return user account id number
     * @throws LogicException if a DBException occurred.
     */
    public int findIdByLogin(String login) throws LogicException {
        AccountDao accountDao = new AccountDao();
        try {
            return accountDao.loadIdByLogin(login);
        } catch (DBException e) {
            throw new LogicException("Can not get accountId by login" + e);
        }
    }

    /**
     * Checks if there is an account with such login
     * @param login login of account to check
     * @return <code>true</code> account was found successfully;
     *         <code>false</code> otherwise.
     * @throws LogicException if a DBException occurred.
     */
    public boolean checkLoginExistence(String login) throws LogicException {
        AccountDao accountDao = new AccountDao();
        try {
            return !accountDao.isLoginUnique(login);
        } catch (DBException e) {
            throw new LogicException("Error while checking if login " + login + " exists. " + e);
        }
    }

    private String encodeWithSHA512(String data, String salt){
        return DigestUtils.sha512Hex(data + salt);
    }
}

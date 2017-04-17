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
 * Here could be your advertisement +375 29 3880490
 */
public class AccountLogic {
    static Logger logger = LogManager.getLogger(AccountLogic.class);

    public AccountLogic() {}

    public boolean checkLogIn(String loginOrEmail, String password) throws LogicException {
        AccountDao accountDao = new AccountDao();
        if (AccountValidator.validateAuthorization(loginOrEmail, password)) {
            try {
                String salt = accountDao.getBirthDateByAuthorization(loginOrEmail);
                password = encodeWithSHA512(password,salt);
                logger.debug(" login " + loginOrEmail + " sha512 password = " + password);
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

    public LogicResult register(String firstName, String lastName, String email, String login, String password,
                                String birthDate, String gender, String avatar, String inviteCode) throws LogicException {
        AccountDao accountDao = new AccountDao();
        LogicResult logicResult = LogicResult.FAILED;
        if(inviteCode==null){
            inviteCode="1234-1234";
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

    public Account getAccountByAuthorization(String loginOrEmail) throws LogicException {
        AccountDao accountDao = new AccountDao();
        try {
            Account account = accountDao.getAccountByAuthorization(loginOrEmail);
            return account;
        } catch (DBException e) {
            throw new LogicException("Error while loading account from database by login or email." + e);
        }
    }

    public Account getAccountByLogin(String login) throws LogicException {
        AccountDao accountDao = new AccountDao();
        try {
            Account account = accountDao.getAccountByLogin(login);
            return account;
        } catch (DBException e) {
            throw new LogicException("Error while loading account from database by login" + e);
        }
    }

    public Account getAccountById(int id) throws LogicException {
        AccountDao accountDao = new AccountDao();
        try {
            Account account = accountDao.getAccountById(id);
            return account;
        } catch (DBException e) {
            throw new LogicException("Error while loading account from database by id " + e);
        }
    }

    public List<Account> getAllAccounts(int accountId) throws LogicException {
        AccountDao accountDao = new AccountDao();
        try {
            List<Account> accounts = accountDao.loadAllAccounts(accountId);
            return accounts;
        } catch (DBException e) {
            throw new LogicException("Error while loading all accounts from database " + e);
        }
    }

    public void changeFirstName(int accountId, String name) throws LogicException {
        AccountDao accountDao = new AccountDao();
        try {
            accountDao.updateFirstName(accountId,name);
        } catch (DBException e) {
            throw new LogicException("Error while updating firstName" + e);
        }
    }

    public void changeLastName(int accountId, String name) throws LogicException {
        AccountDao accountDao = new AccountDao();
        try {
            accountDao.updateLastName(accountId,name);
        } catch (DBException e) {
            throw new LogicException("Error while updating lastName" + e);
        }
    }

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

    public void addToBalance(int accountId, BigDecimal amount) throws LogicException {
        AccountDao accountDao = new AccountDao();
        try {
            Account account = accountDao.getAccountById(accountId);
            accountDao.updateBalance(accountId, account.getBalance().add(amount));
        } catch (DBException e) {
            throw new LogicException("Error while adding some money to balance" + e);
        }
    }

    public void changeBalance(int accountId, BigDecimal balance) throws LogicException{
        AccountDao accountDao = new AccountDao();
        try {
            accountDao.updateBalance(accountId, balance);
        } catch (DBException e) {
            throw new LogicException("Error while updating balance" + e);
        }
    }

    public void changeAvatar(int accountId, String img) throws LogicException {
        AccountDao accountDao = new AccountDao();
        try {
            accountDao.updateAvatar(accountId,img);
        } catch (DBException e) {
            throw new LogicException("Error while updating avatar" + e);
        }
    }

    public void sortByProfit(List<Account> accounts){
        accounts.sort(new ProfitComparator().
                thenComparing(new HandsWonComparator().
                        thenComparing(new RatingComparator())));
    }

    public void sortByRating(List<Account> accounts){
        accounts.sort(new RatingComparator().
                thenComparing(new ProfitComparator().
                        thenComparing(new HandsWonComparator())));
    }

    public void sortByHandsWon(List<Account> accounts){
        accounts.sort(new HandsWonComparator().
                thenComparing(new ProfitComparator().
                        thenComparing(new RatingComparator())));
    }

    public void sortByBanStatus(List<Account> accounts){
        accounts.sort(new BanComparator().
                thenComparing(new ProfitComparator().
                        thenComparing(new RatingComparator())));
    }

    public void banAccount(int accountId, boolean isBanned) throws LogicException{
        AccountDao accountDao = new AccountDao();
        try {
            accountDao.updateBanStatus(accountId,isBanned);
        } catch (DBException e) {
            throw new LogicException("Error while banning user " + e);
        }
    }

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

    public void setAdminRole(int accountId, boolean isAdmin) throws LogicException{
        AccountDao accountDao = new AccountDao();
        try {
            accountDao.updateAdminRole(accountId,isAdmin);
        } catch (DBException e) {
            throw new LogicException("Error while updating admin role " + e);
        }
    }

    public void changeStatistics(int accountId, int played, int handsWon, BigDecimal moneySpend, BigDecimal moneyWon, int rating) throws LogicException {
        AccountDao accountDao = new AccountDao();
        try {
            accountDao.updateStatistics(accountId,played,handsWon,moneySpend,moneyWon,rating);
        } catch (DBException e) {
            throw new LogicException("Error while updating statistics " + e);
        }
    }

    private String encodeWithSHA512(String data, String salt){
        return DigestUtils.sha512Hex(data + salt);
    }
}

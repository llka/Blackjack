package ru.ilka.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.datebase.ConnectionPool;
import ru.ilka.entity.Account;
import ru.ilka.entity.GenderType;
import ru.ilka.exception.DBException;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class AccountDao {
    static  Logger logger = LogManager.getLogger(AccountDao.class);
    
    private static final String FIND_LOGIN = "SELECT `login` FROM `users` WHERE `login` = ?";
    private static final String FIND_EMAIL = "SELECT `email_address` FROM `users` WHERE `email_address` = ?";
    private static final String FIND_DATE_FROM_AUTHORIZE = "SELECT `birth_date` FROM `users` WHERE (`login` = ? OR `email_address` = ?)";
    private static final String AUTHORIZE_CHECK = "SELECT `login` FROM `users` WHERE ((`login` = ? OR `email_address` = ?) AND `password` = ?)";
    private static final String ADD_USER_ACCOUNT = "INSERT INTO `users` (`first_name`, `last_name`, `email_address`, `login`, `password`, `birth_date`," +
            " `gender`, `avatar`, `invite_code`, `is_admin`, `ban`, `balance`, `played`, `hands_won`, `money_spend`, `money_won`, `rating`)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, FALSE, FALSE, 0.00, 0, 0, 0.0, 0, 0)";

    private static final String GET_ACCOUNT_BY_LOGIN = "SELECT `account_id`, `first_name`, `last_name`, `email_address`, `password`, `birth_date`," +
            " `gender`, `avatar`, `invite_code`, `is_admin`, `ban`, `balance`, `played`, `hands_won`, `money_spend`, `money_won`, `rating` FROM `users` WHERE `login` = ?";
    private static final String GET_ACCOUNT_BY_AUTHORIZATION = "SELECT `account_id`, `first_name`, `last_name`, `email_address`, `login`, `password`, `birth_date`," +
            " `gender`, `avatar`, `invite_code`, `is_admin`, `ban`, `balance`, `played`, `hands_won`, `money_spend`, `money_won`, `rating` FROM `users`  WHERE (`login` = ? OR `email_address` = ?)";
    private static final String GET_ACCOUNT_BY_ID= "SELECT `account_id`, `first_name`, `last_name`, `email_address`,`login`, `password`, `birth_date`," +
            " `gender`, `avatar`, `invite_code`, `is_admin`, `ban`, `balance`, `played`, `hands_won`, `money_spend`, `money_won`, `rating` FROM `users` WHERE `account_id` = ?";
    private static final String LOAD_ALL_ACCOUNTS = "SELECT `account_id`, `email_address`,`login`," +
            " `invite_code`, `is_admin`, `ban`, `balance`, `played`, `hands_won`, `money_spend`, `money_won`, `rating` FROM `users` WHERE `account_id` != ?";

    private static final String UPDATE_FIRST_NAME = "UPDATE `users` SET `first_name` = ?  WHERE `account_id` = ?";
    private static final String UPDATE_LAST_NAME = "UPDATE `users` SET `last_name` = ?  WHERE `account_id` = ?";
    private static final String UPDATE_EMAIL = "UPDATE `users` SET `email_address` = ?  WHERE `account_id` = ?";
    private static final String UPDATE_LOGIN= "UPDATE `users` SET `login` = ?  WHERE `account_id` = ?";
    private static final String UPDATE_PASSWORD= "UPDATE `users` SET `password` = ?  WHERE `account_id` = ?";
    private static final String UPDATE_BALANCE= "UPDATE `users` SET `balance` = ?  WHERE `account_id` = ?";
    private static final String UPDATE_AVATAR= "UPDATE `users` SET `avatar` = ?  WHERE `account_id` = ?";
    private static final String UPDATE_BAN_STATUS= "UPDATE `users` SET `ban` = ?  WHERE `account_id` = ?";
    private static final String UPDATE_ADMIN_ROLE= "UPDATE `users` SET `is_admin` = ?  WHERE `account_id` = ?";


    private static final String COLUMN_ACCOUNT_ID = "account_id";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_EMAIL = "email_address";
    private static final String COLUMN_LOGIN = "login";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_BIRTH_DATE = "birth_date";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_INVITE_CODE = "invite_code";
    private static final String COLUMN_IS_ADMIN = "is_admin";
    private static final String COLUMN_AVATAR = "avatar";
    private static final String COLUMN_HAS_BAN= "ban";
    private static final String COLUMN_BALANCE = "balance";
    private static final String COLUMN_HANDS_PLAYED= "played";
    private static final String COLUMN_HANDS_WON = "hands_won";
    private static final String COLUMN_MONEY_SPEND= "money_spend";
    private static final String COLUMN_MONEY_WON = "money_won";
    private static final String COLUMN_RATING= "rating";

    public AccountDao() {
    }

    /*
    Авторизацию не следует путать с аутентификацией: аутентификация — это процедура проверки легальности пользователя
    или данных, например, проверки соответствия введённого пользователем пароля к учётной записи паролю в базе данных,
    или проверка цифровой подписи письма по ключу шифрования, или проверка контрольной суммы файла на соответствие
    заявленной автором этого файла. Авторизация же производит контроль доступа легальных пользователей к ресурсам
    системы после успешного прохождения ими аутентификации. Зачастую процедуры аутентификации и авторизации совмещаются.
     */
    public boolean authorizeUser(String loginOrEmail, String password) throws DBException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
              PreparedStatement preparedStatement = connection.prepareStatement(AUTHORIZE_CHECK)) {
            preparedStatement.setString(1, loginOrEmail);
            preparedStatement.setString(2, loginOrEmail);
            preparedStatement.setString(3, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new DBException("Error while checking account in database." + e);
        }
    }

    public String getBirthDateByAuthorization(String loginOrEmail) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_DATE_FROM_AUTHORIZE)){
            preparedStatement.setString(1,loginOrEmail);
            preparedStatement.setString(2,loginOrEmail);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getDate(COLUMN_BIRTH_DATE).toString();
            }else {
                throw new DBException("No such loginOrEmail (salt) in database. " + loginOrEmail);
            }
        } catch (SQLException e) {
            throw new DBException("Error while finding login (salt) in database." + e);
        }
    }

    public void register(String firstName, String lastName, String email, String login, String password,
                            String birthDate, String gender, String avatar, String inviteCode) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(ADD_USER_ACCOUNT)){
            preparedStatement.setString(1,firstName);
            preparedStatement.setString(2,lastName);
            preparedStatement.setString(3,email);
            preparedStatement.setString(4,login);
            preparedStatement.setString(5,password);
            preparedStatement.setString(6,birthDate);
            preparedStatement.setString(7,gender);
            preparedStatement.setString(8,avatar);
            preparedStatement.setString(9,inviteCode);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DBException("Error while inserting new account into database." + e);
        }
    }

    public boolean isLoginUnique(String login) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_LOGIN)){
            preparedStatement.setString(1,login);
            ResultSet resultSet = preparedStatement.executeQuery();
            return  !resultSet.next();
        } catch (SQLException e) {
            throw new DBException("Error while checking login Uniqueness in database." + e);
        }
    }

    public boolean isEmailUnique(String email) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_EMAIL)){
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return  !resultSet.next();
        } catch (SQLException e) {
            throw new DBException("Error while checking email Uniqueness in database." + e);
        }
    }

    public Account getAccountByLogin(String login) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(GET_ACCOUNT_BY_LOGIN)){
            preparedStatement.setString(1,login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
               Account account = new Account();
               account.setAccountId(resultSet.getInt(COLUMN_ACCOUNT_ID));
               account.setFirstName(resultSet.getString(COLUMN_FIRST_NAME));
               account.setLastName(resultSet.getString(COLUMN_LAST_NAME));
               account.setEmail(resultSet.getString(COLUMN_EMAIL));
               account.setLogin(login);
               account.setPassword(resultSet.getString(COLUMN_PASSWORD));
               account.setBirthDate(resultSet.getDate(COLUMN_BIRTH_DATE));
               account.setGender(GenderType.valueOf(resultSet.getString(COLUMN_GENDER).toUpperCase()));
               account.setInviteCode(resultSet.getString(COLUMN_INVITE_CODE));
               account.setAvatar(resultSet.getString(COLUMN_AVATAR));
               account.setAdmin(resultSet.getBoolean(COLUMN_IS_ADMIN));
               account.setBan(resultSet.getBoolean(COLUMN_HAS_BAN));
               account.setBalance(resultSet.getBigDecimal(COLUMN_BALANCE));
               account.setHandsPlayed(resultSet.getInt(COLUMN_HANDS_PLAYED));
               account.setHandsWon(resultSet.getInt(COLUMN_HANDS_WON));
               account.setMoneySpend(resultSet.getBigDecimal(COLUMN_MONEY_SPEND));
               account.setMoneyWon(resultSet.getBigDecimal(COLUMN_MONEY_WON));
               account.setRating(resultSet.getInt(COLUMN_RATING));
               return account;
            }else {
                throw new DBException("No account with such login " + login + " found in database.");
            }
        } catch (SQLException e) {
            throw new DBException("Error while finding account " + login + " in database." + e);
        }
    }

    public Account getAccountById(int id) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(GET_ACCOUNT_BY_ID)){
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                Account account = new Account();
                account.setAccountId(id);
                account.setFirstName(resultSet.getString(COLUMN_FIRST_NAME));
                account.setLastName(resultSet.getString(COLUMN_LAST_NAME));
                account.setEmail(resultSet.getString(COLUMN_EMAIL));
                account.setLogin(resultSet.getString(COLUMN_LOGIN));
                account.setPassword(resultSet.getString(COLUMN_PASSWORD));
                account.setBirthDate(resultSet.getDate(COLUMN_BIRTH_DATE));
                account.setGender(GenderType.valueOf(resultSet.getString(COLUMN_GENDER).toUpperCase()));
                account.setAvatar(resultSet.getString(COLUMN_AVATAR));
                account.setInviteCode(resultSet.getString(COLUMN_INVITE_CODE));
                account.setAdmin(resultSet.getBoolean(COLUMN_IS_ADMIN));
                account.setBan(resultSet.getBoolean(COLUMN_HAS_BAN));
                account.setBalance(resultSet.getBigDecimal(COLUMN_BALANCE));
                account.setHandsPlayed(resultSet.getInt(COLUMN_HANDS_PLAYED));
                account.setHandsWon(resultSet.getInt(COLUMN_HANDS_WON));
                account.setMoneySpend(resultSet.getBigDecimal(COLUMN_MONEY_SPEND));
                account.setMoneyWon(resultSet.getBigDecimal(COLUMN_MONEY_WON));
                account.setRating(resultSet.getInt(COLUMN_RATING));
                return account;
            }else {
                throw new DBException("No account with such account_id " + id + " found in database.");
            }
        } catch (SQLException e) {
            throw new DBException("Error while finding account " + id + " in database." + e);
        }
    }

    public Account getAccountByAuthorization(String loginOrEmail) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(GET_ACCOUNT_BY_AUTHORIZATION)){
            preparedStatement.setString(1,loginOrEmail);
            preparedStatement.setString(2,loginOrEmail);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Account account = new Account();
                account.setAccountId(resultSet.getInt(COLUMN_ACCOUNT_ID));
                account.setFirstName(resultSet.getString(COLUMN_FIRST_NAME));
                account.setLastName(resultSet.getString(COLUMN_LAST_NAME));
                account.setEmail(resultSet.getString(COLUMN_EMAIL));
                account.setLogin(resultSet.getString(COLUMN_LOGIN));
                account.setPassword(resultSet.getString(COLUMN_PASSWORD));
                account.setBirthDate(resultSet.getDate(COLUMN_BIRTH_DATE));
                account.setGender(GenderType.valueOf(resultSet.getString(COLUMN_GENDER).toUpperCase()));
                account.setAvatar(resultSet.getString(COLUMN_AVATAR));
                account.setInviteCode(resultSet.getString(COLUMN_INVITE_CODE));
                account.setAdmin(resultSet.getBoolean(COLUMN_IS_ADMIN));
                account.setBan(resultSet.getBoolean(COLUMN_HAS_BAN));
                account.setBalance(resultSet.getBigDecimal(COLUMN_BALANCE));
                account.setHandsPlayed(resultSet.getInt(COLUMN_HANDS_PLAYED));
                account.setHandsWon(resultSet.getInt(COLUMN_HANDS_WON));
                account.setMoneySpend(resultSet.getBigDecimal(COLUMN_MONEY_SPEND));
                account.setMoneyWon(resultSet.getBigDecimal(COLUMN_MONEY_WON));
                account.setRating(resultSet.getInt(COLUMN_RATING));
                return account;
            }else {
                throw new DBException("No account with such login " + loginOrEmail + " found in database.");
            }
        } catch (SQLException e) {
            throw new DBException("Error while finding account " + loginOrEmail + " in database." + e);
        }
    }

    public List<Account> loadAllAccounts(int accountId) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(LOAD_ALL_ACCOUNTS)) {
            preparedStatement.setInt(1,accountId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Account> accounts = new ArrayList<>();
            while (resultSet.next()) {
                Account account = new Account();
                account.setAccountId(resultSet.getInt(COLUMN_ACCOUNT_ID));
                account.setEmail(resultSet.getString(COLUMN_EMAIL));
                account.setLogin(resultSet.getString(COLUMN_LOGIN));
                account.setInviteCode(resultSet.getString(COLUMN_INVITE_CODE));
                account.setAdmin(resultSet.getBoolean(COLUMN_IS_ADMIN));
                account.setBan(resultSet.getBoolean(COLUMN_HAS_BAN));
                account.setBalance(resultSet.getBigDecimal(COLUMN_BALANCE));
                account.setHandsPlayed(resultSet.getInt(COLUMN_HANDS_PLAYED));
                account.setHandsWon(resultSet.getInt(COLUMN_HANDS_WON));
                account.setMoneySpend(resultSet.getBigDecimal(COLUMN_MONEY_SPEND));
                account.setMoneyWon(resultSet.getBigDecimal(COLUMN_MONEY_WON));
                account.setRating(resultSet.getInt(COLUMN_RATING));
                accounts.add(account);
            }
            //return Collections.unmodifiableList(accounts);
            return accounts;
        }catch(SQLException e) {
            throw new DBException("Error while loading all accounts from database." + e);
        }
    }

    public void updateFirstName(int accountId, String name) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_FIRST_NAME)) {
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,accountId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DBException("Error while updating first name in" + accountId + " in database." + e);
        }
    }
    
    public void updateLastName(int accountId, String surname) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_LAST_NAME)) {
            preparedStatement.setString(1,surname);
            preparedStatement.setInt(2,accountId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DBException("Error while updating last name in " + accountId + " in database." + e);
        }
    }

    public void updateEmail(int accountId, String email) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EMAIL)) {
            preparedStatement.setString(1,email);
            preparedStatement.setInt(2,accountId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DBException("Error while updating email in " + accountId + " in database." + e);
        }
    }

    public void updateLogin(int accountId, String login) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_LOGIN)) {
            preparedStatement.setString(1,login);
            preparedStatement.setInt(2,accountId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DBException("Error while updating login in " + accountId + " in database." + e);
        }
    }

    public void updatePassword(int accountId, String password) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PASSWORD)) {
            preparedStatement.setString(1,password);
            preparedStatement.setInt(2,accountId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DBException("Error while updating password in " + accountId + " in database." + e);
        }
    }

    public void updateBalance(int accountId, BigDecimal balance) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BALANCE)) {
            preparedStatement.setBigDecimal(1,balance);
            preparedStatement.setInt(2,accountId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DBException("Error while updating balance in " + accountId + " in database." + e);
        }
    }

    public void updateAvatar(int accountId, String avatar) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_AVATAR)) {
            preparedStatement.setString(1,avatar);
            preparedStatement.setInt(2,accountId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DBException("Error while updating avatar in " + accountId + " in database." + e);
        }
    }

    public void updateBanStatus(int accountId, boolean ban) throws DBException{
        try(Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BAN_STATUS)) {
            preparedStatement.setBoolean(1,ban);
            preparedStatement.setInt(2, accountId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DBException("Error while banning account № " + accountId + " in database." + e);
        }
    }

    public void updateAdminRole(int accountId, boolean isAdmin) throws DBException{
        try(Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ADMIN_ROLE)) {
            preparedStatement.setBoolean(1,isAdmin);
            preparedStatement.setInt(2, accountId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DBException("Error while updating admin role in account № " + accountId + " " + e);
        }
    }
}

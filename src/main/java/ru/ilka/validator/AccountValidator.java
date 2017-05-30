package ru.ilka.validator;

/**
 * AccountValidator class provides functionality for validating strings which take part in account creating.
 * @since %G%
 * @version %I%
 */
public class AccountValidator {
    private static final String LOGIN_REGEX = "[a-zA-Z0-9_]{4,45}";
    private static final String EMAIL_REGEX = "([a-zA-Z0-9._-]+)*[a-zA-Z0-9._-]+@[a-z0-9_-]+([a-z0-9_-]+)*([.]{1})[a-z]{2,6}";
    private static final String PASSWORD_REGEX = "(?=^.{6,40}$)(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?!.*\\s)(?!.*\\W).*$";
    private static final String GENDER_REGEX = "(Male)|(Female)|(Женщина)|(Мужчина)";
    private static final String INVITE_CODE_REGEX = "[0-9A-Z]{4}[-]{1}[0-9A-Z]{4}";


    public static boolean validateLogin(String login) {
        return login.matches(LOGIN_REGEX);
    }

    public static boolean validateEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }

    public static boolean validatePassword(String password) {
        return password.matches(PASSWORD_REGEX);
    }

    public static boolean validateAuthorization(String loginOrEmail, String password) {
        return (validateLogin(loginOrEmail) || validateEmail(loginOrEmail)) && validatePassword(password);
    }

    public static boolean validateGender(String gender){
        return  gender.matches(GENDER_REGEX);
    }

    public static boolean validateInviteCode(String code){
        return  code.matches(INVITE_CODE_REGEX);
    }
}
package ru.ilka.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class Account {
    private long accountId;
    private String firstName;
    private String lastName;
    private String email;
    private String login;
    private String password;
    private Date birthDate;
    private GenderType gender;
    private String avatar;
    private String inviteCode;
    private boolean admin;
    private boolean ban;
    private BigDecimal balance;
    private int handsPlayed;
    private int handsWon;
    private BigDecimal moneySpend;
    private BigDecimal moneyWon;
    private int rating;

    public Account(){

    }

    public Account(long accountId, String firstName, String lastName, String email, String login, Date birthDate, GenderType gender,
                   String avatar, String inviteCode, boolean admin, boolean ban, BigDecimal balance,String password,
                   int handsPlayed, int handsWon, BigDecimal moneySpend, BigDecimal moneyWon, int rating) {
        this.accountId = accountId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.login = login;
        this.password = password;
        this.birthDate = birthDate;
        this.gender = gender;
        this.avatar = avatar;
        this.inviteCode = inviteCode;
        this.admin = admin;
        this.ban = ban;
        this.balance = new BigDecimal(0);
        this.balance.add(balance);
        this.balance.setScale(2,BigDecimal.ROUND_FLOOR);
        this.balance = new BigDecimal(0);
        this.handsPlayed = handsPlayed;
        this.handsWon = handsWon;
        this.moneySpend = moneySpend;
        this.moneySpend.setScale(2,BigDecimal.ROUND_FLOOR);
        this.moneyWon = moneyWon;
        this.moneyWon.setScale(2,BigDecimal.ROUND_FLOOR);
        this.rating = rating;
    }

    public Account(long accountId, String firstName, String lastName, String email, String login, String password,
                   Date birthDate, GenderType gender, String avatar, String inviteCode) {
        this.accountId = accountId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.login = login;
        this.password = password;
        this.birthDate = birthDate;
        this.gender = gender;
        this.avatar = avatar;
        this.inviteCode = inviteCode;
        this.admin = false;
        this.ban = false;
        this.balance = new BigDecimal(0);
        this.balance.setScale(2,BigDecimal.ROUND_FLOOR);
        this.handsPlayed = 0;
        this.handsWon = 0;
        this.moneySpend = new BigDecimal(0);
        this.moneySpend.setScale(2,BigDecimal.ROUND_FLOOR);
        this.moneyWon = new BigDecimal(0);
        this.moneyWon.setScale(2,BigDecimal.ROUND_FLOOR);
        this.rating = 0;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public GenderType getGender() {
        return gender;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isBan() {
        return ban;
    }

    public void setBan(boolean ban) {
        this.ban = ban;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getHandsPlayed() {
        return handsPlayed;
    }

    public void setHandsPlayed(int handsPlayed) {
        this.handsPlayed = handsPlayed;
    }

    public int getHandsWon() {
        return handsWon;
    }

    public void setHandsWon(int handsWon) {
        this.handsWon = handsWon;
    }

    public BigDecimal getMoneySpend() {
        return moneySpend;
    }

    public void setMoneySpend(BigDecimal moneySpend) {
        this.moneySpend = moneySpend;
    }

    public BigDecimal getMoneyWon() {
        return moneyWon;
    }

    public void setMoneyWon(BigDecimal moneyWon) {
        this.moneyWon = moneyWon;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        Account account = (Account) o;

        if (accountId != account.accountId) return false;
        if (admin != account.admin) return false;
        if (ban != account.ban) return false;
        if (handsPlayed != account.handsPlayed) return false;
        if (handsWon != account.handsWon) return false;
        if (rating != account.rating) return false;
        if (!firstName.equals(account.firstName)) return false;
        if (!lastName.equals(account.lastName)) return false;
        if (!email.equals(account.email)) return false;
        if (!login.equals(account.login)) return false;
        if (!birthDate.equals(account.birthDate)) return false;
        if (gender != account.gender) return false;
        if (avatar != null ? !avatar.equals(account.avatar) : account.avatar != null) return false;
        if (inviteCode != null ? !inviteCode.equals(account.inviteCode) : account.inviteCode != null) return false;
        if (!balance.equals(account.balance)) return false;
        if (!moneySpend.equals(account.moneySpend)) return false;
        return moneyWon.equals(account.moneyWon);
    }

    @Override
    public int hashCode() {
        int result = (int) (accountId ^ (accountId >>> 32));
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + login.hashCode();
        result = 31 * result + birthDate.hashCode();
        result = 31 * result + gender.hashCode();
        result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
        result = 31 * result + (inviteCode != null ? inviteCode.hashCode() : 0);
        result = 31 * result + (admin ? 1 : 0);
        result = 31 * result + (ban ? 1 : 0);
        result = 31 * result + balance.hashCode();
        result = 31 * result + handsPlayed;
        result = 31 * result + handsWon;
        result = 31 * result + moneySpend.hashCode();
        result = 31 * result + moneyWon.hashCode();
        result = 31 * result + rating;
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                /*", birthDate=" + birthDate +
                ", gender=" + gender +
                ", avatar=" + avatar +
                ", inviteCode='" + inviteCode + '\'' +
                ", admin=" + admin +
                ", ban=" + ban +
                ", balance=" + balance +
                ", handsPlayed=" + handsPlayed +
                ", handsWon=" + handsWon +
                ", moneySpend=" + moneySpend +
                ", moneyWon=" + moneyWon +
                ", rating=" + rating +*/
                '}';
    }
}

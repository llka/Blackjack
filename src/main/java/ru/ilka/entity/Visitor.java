package ru.ilka.entity;

import java.util.Locale;

/**
 * A Visitor object encapsulates the state information of random system visitor independently of his role
 * @see Account
 * @since %G%
 * @version %I%
 */
public class Visitor {
    /**
     * Roles that can be used
     */
    public enum Role {
        /**
         * administrator is the most influential role
         */
        ADMIN,
        /**
         * ordinary registered visitor, all functions available
         */
        USER,
        /**
         * not registered visitor, can stay only on greeting page
         */
        GUEST
    }

    private Role role;
    private Locale locale;
    private String previousPage;
    private String currentPage;
    private String name;
    private boolean sessionLost;

    public Visitor() {
        this.sessionLost = false;
    }

    public Visitor(Role role, Locale locale, String currentPage) {
        this.role = role;
        this.locale = locale;
        this.currentPage = currentPage;
        this.sessionLost = false;
    }

    public String getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(String previousPage) {
        this.previousPage = previousPage;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSessionLost() {
        return sessionLost;
    }

    public void setSessionLost(boolean sessionLost) {
        this.sessionLost = sessionLost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Visitor visitor = (Visitor) o;

        if (role != visitor.role) return false;
        if (!locale.equals(visitor.locale)) return false;
        return currentPage.equals(visitor.currentPage);
    }

    @Override
    public int hashCode() {
        int result = role.hashCode();
        result = 31 * result + locale.hashCode();
        result = 31 * result + currentPage.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Visitor{" +
                "role=" + role +
                ", locale=" + locale +
                '}';
    }
}

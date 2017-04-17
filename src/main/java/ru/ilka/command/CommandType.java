package ru.ilka.command;

import ru.ilka.command.admin.*;
import ru.ilka.command.guest.LogInCommand;
import ru.ilka.command.guest.RegisterCommand;
import ru.ilka.command.user.*;
import ru.ilka.entity.Visitor;

import java.util.EnumSet;
import java.util.Set;

/**
 * Here could be your advertisement +375 29 3880490
 */
public enum CommandType {
    LOGIN {
        {
            this.command = new LogInCommand();
            this.role = EnumSet.of(Visitor.Role.GUEST);
        }
    },
    REGISTER {
        {
            this.command = new RegisterCommand();
            this.role = EnumSet.of(Visitor.Role.GUEST);
        }
    },
    INFO {
        {
            this.command = new AccountInfoCommand();
            this.role = EnumSet.of(Visitor.Role.USER,Visitor.Role.ADMIN);
        }
    },
    LOGOUT {
        {
            this.command = new LogOutCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN, Visitor.Role.USER);
        }
    },
    CHANGELANGUAGE {
        {
            this.command = new ChangeLocaleCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN, Visitor.Role.USER, Visitor.Role.GUEST);
        }
    },
    CHANGEAVATAR{
        {
            this.command = new ChangeAvatarCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN, Visitor.Role.USER);
        }
    },
    ADDBALANCE{
        {
            this.command = new AddBalanceCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN, Visitor.Role.USER);
        }
    },
    CHECKPAYCARD{
        {
            this.command = new CheckPayCardCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN, Visitor.Role.USER);
        }
    },
    SORTBYRATING{
        {
            this.command = new SortByRatingCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN);
        }
    },
    SORTBYHANDSWON{
        {
            this.command = new SortByHandsWonCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN);
        }
    },
    SORTBYPROFIT{
        {
            this.command = new SortByProfitCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN);
        }
    },
    SORTBYBANSTATUS{
        {
            this.command = new SortByBanStatusCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN);
        }
    },
    GLOBALBAN{
        {
            this.command = new GlobalBanCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN);
        }
    },
    BANUSERS{
        {
            this.command = new BanUsersCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN);
        }
    },
    CHANGESETTINGS{
        {
            this.command = new ChangeSettingsCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN);
        }
    },
    NEWGAME{
        {
            this.command = new NewGameCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN, Visitor.Role.USER);
        }
    },
    CHECKFORINSURANCE{
        {
            this.command = new CheckForInsuranceCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN, Visitor.Role.USER);
        }
    },
    INSUREBET{
        {
            this.command = new InsureBetCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN, Visitor.Role.USER);
        }
    },
    HITCARD{
        {
            this.command = new HitCardCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN, Visitor.Role.USER);
        }
    },
    SHOWACTIONBTN{
        {
            this.command = new ShowActionBtnCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN, Visitor.Role.USER);
        }
    },
    DEALCARDS{
        {
            this.command = new DealCardsCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN, Visitor.Role.USER);
        }
    };

    public ActionCommand command;
    public Set<Visitor.Role> role;

    public ActionCommand getCurrentCommand() {
        return command;
    }
}

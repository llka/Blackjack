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
    STAND{
        {
            this.command = new StandCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN, Visitor.Role.USER);
        }
    },
    IMMEDIATEBJWIN{
        {
            this.command = new ImmediateBjWinCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN, Visitor.Role.USER);
        }
    },
    SURRENDERBET{
        {
            this.command = new SurrenderBetCommand();
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
    },
    MANAGEMESSAGES{
        {
            this.command = new ManageMessagesCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN,Visitor.Role.USER);
        }
    },
    SENDUSMESSAGE{
        {
            this.command = new SendUsMessageCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN,Visitor.Role.USER);
        }
    },
    MARKMESSAGE{
        {
            this.command = new MarkMessageCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN,Visitor.Role.USER);
        }
    },
    DELETEMESSAGE{
        {
            this.command = new DeleteMessageCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN,Visitor.Role.USER);
        }
    },
    GAMESHISTORY{
        {
            this.command = new GamesHistoryCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN,Visitor.Role.USER);
        }
    },
    SENDMESSAGE{
        {
            this.command = new SendMessageCommand();
            this.role = EnumSet.of(Visitor.Role.ADMIN,Visitor.Role.USER);
        }
    };

    public ActionCommand command;
    public Set<Visitor.Role> role;

    public ActionCommand getCurrentCommand() {
        return command;
    }
}

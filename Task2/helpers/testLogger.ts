import { LogTag } from "./logTag";

export class TestLogger {
    private className: string;

    private static readonly RESET = '\u001B[0m';
    private static readonly GREEN = '\u001B[32m';
    private static readonly BLUE = '\u001B[34m';
    private static readonly RED = '\u001B[31m';
    private static readonly YELLOW = '\u001B[3m';
    private static readonly ORANGE = '\u001B[38;5;208m';

    constructor(className: string) {
        this.className = className;
    }

    private colorize(logTag: LogTag, message: string) {
        switch (logTag) {
            case LogTag.ACTION_MARKER:
                return TestLogger.BLUE + message + TestLogger.RESET;
            case LogTag.CHECK_MARKER:
                return TestLogger.GREEN + message + TestLogger.RESET;
            case LogTag.INFO_MARKER:
                return TestLogger.YELLOW + message + TestLogger.RESET;
            case LogTag.ERROR_MARKER:
                return TestLogger.RED + message + TestLogger.RESET;
            case LogTag.HOOK_MARKER:
                return TestLogger.ORANGE + message + TestLogger.RESET;
            default:
                return message;
        }
    }

    private formatMessage(logTag: LogTag, message: string, ...args: any[]): string {
        const timeStamp = new Date().toISOString();
        const formatMessage = this.sprintf(message, ...args);
        return `[${timeStamp}] [${this.className}] ${logTag} ${formatMessage}`;
    }

    private sprintf(format: string, ...args: any[]): string {
        let formatted = format;
        for(let i =0; i<args.length; i++) {
            formatted = formatted.replace(/%s|%d|%o/, String(args[i]));
        }
        return formatted;
    }

    public info(logTag: LogTag, message: string, ...args: any[]): void {
        const formattedMessage = this.formatMessage(logTag, message, args);
        const colorizedMessage = this.colorize(logTag, formattedMessage);
        console.log(colorizedMessage);
    }

    public error(logTag: LogTag, message: string, ...args: any[]): void {
        const formattedMessage = this.formatMessage(logTag, message, args);
        const colorizedMessage = this.colorize(logTag, formattedMessage);
        console.error(colorizedMessage);
    }

    public action(message: string, ...args: any[]): void {
        this.info(LogTag.ACTION_MARKER, message, args);
    }
    public check(message: string, ...args: any[]): void {
        this.info(LogTag.CHECK_MARKER, message, args);
    }

    public infoLog(message: string, ...args: any[]): void {
        this.info(LogTag.INFO_MARKER, message, args);
    }

    public errorLog(message: string, ...args: any[]): void {
        this.info(LogTag.ERROR_MARKER, message, args);
    }

    public hook(message: string, ...args: any[]): void {
        this.info(LogTag.HOOK_MARKER, message, args);
    }

    public startHookMessage(hookType: string): void {
        const message = `---${hookType.toUpperCase()} START---`;
        this.hook(message);
    }

    public endHookMessage(hookType: string): void {
        const message = `---${hookType.toUpperCase()} END---`;
        this.hook(message);
    }
}
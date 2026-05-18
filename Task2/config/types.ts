export interface Config {
    credentials: Credentials;
    environement: Environement;
}

export interface Credentials {
    email: string;
    password: string;
    firstName: string;
    lastName: string;
    incorrectPassword: string;
}

export interface Environement {
    baseUrl: string;
    loginPageConfig: PageConfig;
    shoppingPageConfig: PageConfig;
}

export interface PageConfig {
    url: string;
    defaultWatingTime: number;
}
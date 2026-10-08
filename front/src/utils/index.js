import { post } from './ajax';
import { get } from './ajax';

//后台接口地址
const baseUrl = 'http://127.0.0.1:8776/api/back/';


//登录
export const login = data => post(`${baseUrl}login`, data);

export const list = data => post(`${baseUrl}list`, data);

export const commentList = data => post(`${baseUrl}commentList`, data);

export const del = data => post(`${baseUrl}del`, data);

export const fenjin = data => post(`${baseUrl}fenjin`, data);

export const adminList = data => post(`${baseUrl}adminList`, data);

export const addAdmin = data => post(`${baseUrl}addAdmin`, data);

export const userList = data => post(`${baseUrl}userList`, data);

export const logLst = data => post(`${baseUrl}logList`, data);

export const sensitive = data => post(`${baseUrl}sensitive`, data);
export const addSensitiveApi = data => post(`${baseUrl}addsensitive`, data);














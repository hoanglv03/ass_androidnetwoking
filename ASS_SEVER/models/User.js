var db = require('../config/db');
const jwt = require('jsonwebtoken')
require('dotenv').config()
const keyPrivate = process.env.TOKEN_KEY;
const bcrypt = require("bcrypt");


var account = new db.mongoose.Schema(
    {
        userName: {type:String},
        passWord: {type:String},
        email: {type:String},
        vaiTro: {type:String,default:'Người dùng'},
        tokens: {type:String},
        ngayTao : {type:Date,default: Date.now}
    },{
        collection:"account"
    }

)
account.methods.generateAuthToken = async function () {
    const user = this
    const token = jwt.sign({_id: user._id, userName: user.userName}, keyPrivate)
    // user.tokens = user.tokens.concat({token}) // code này dành cho nhiều token, ở demo này dùng 1 token
    user.tokens = token;
    await user.save()
    return token
 }
// dùng cho đăng nhập
account.statics.findByCredentials = async (email, passwd) => {
    console.log(email,passwd);
    const users = await accountModel.findOne({email:email})
    let result = '';
    if (!users) {
        result = 'failure'
        return {users,message:'Sai thông tin đăng nhập',result}
    }
    const isPasswordMatch = await bcrypt.compare(passwd, users.passWord)
    if (!isPasswordMatch) {
        result = 'failure'
        return {users,message:'Sai mật khẩu',result}
    }
    result = 'success'
    return {users,message:'Đăng nhập thành công',result}
 }
 


let accountModel = db.mongoose.model('account',account)
module.exports = {accountModel}
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
account.statics.findByCredentials = async (username, passwd) => {
    console.log(username,passwd);
    const user = await accountModel.findOne({userName:username})
    if (!user) {
        throw new Error({error: 'Không tồn tại user'})
    }
    const isPasswordMatch = await bcrypt.compare(passwd, user.passWord)
    if (!isPasswordMatch) {
        throw new Error({error: 'Sai password'})
    }
    return user
 }
 


let accountModel = db.mongoose.model('account',account)
module.exports = {accountModel}
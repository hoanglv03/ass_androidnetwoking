const jwt = require('jsonwebtoken')
const md = require('../models/User');
require('dotenv').config(); // su dung thu vien doc file env
const keyPrivate = process.env.TOKEN_KEY;

const api_auth = async(req, res, next) => {
   let header_token = req.header('Authorization');
   if(typeof(header_token) =='undefined'){
       return res.status(403).json({msg: 'Không xác định token'});
   }
   const token = header_token.replace('Bearer ', '')
   console.log(token);
   try {
       const data = jwt.verify(token, keyPrivate)
       console.log(data);
       const user = await md.accountModel.findOne({ _id: data._id, tokens: token })
       if (!user) {
           throw new Error("Không xác định được người dùng")
       }
       req.user = user
       req.token = token
       next();
   } catch (error) {
       console.log(error);
       res.status(401).send({ error: error.message })
   }
}
module.exports = {api_auth}



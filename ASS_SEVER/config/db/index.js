const mongoose = require('mongoose');
mongoose.connect('mongodb://localhost:27017/ASS_SEVER').catch(err=>{
            console.log("Connect thành công");
            console.log(err);
        });
module.exports = {mongoose}
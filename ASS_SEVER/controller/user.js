var User = require('../models/User');
var moment = require('moment');

const bcrypt = require('bcrypt'); 
exports.deleteUser = async (req, res, next) => {
    await User.accountModel.deleteOne({ _id: req.params.id })
    console.log("đã vào delete");
    res.redirect('back')
}

exports.edit = async (req, res, next) => {
    let msg = '';
    let idUser = req.params.id
    if (req.method == "POST") {
        let newUser = new User.accountModel();
        const salt = await bcrypt.genSalt(15);
        console.log("Chuoi ngau nhien =  " + salt );
        newUser._id = idUser;
        newUser.userName = req.body.userName.trim()
        newUser.passWord = await bcrypt.hash(req.body.passWord.trim() , salt );;
        newUser.vaiTro = req.body.vaiTro;
        try {
            await User.accountModel.findByIdAndUpdate({ _id: idUser }, newUser)
            console.log("Sửa thành công");
            msg = "Sửa thành công"
        } catch (error) {
            msg = "Sửa thất bại" + error.message;
            console.log(error);
        }
    }

    let account = await User.accountModel.findById(idUser);
    res.render('home/editUser', { user: account })
}
exports.listUser = async (req, res, next) => {
    let error = 0;
    let listUser = await User.accountModel.find();
    let msg = '';
    if (req.method == "POST") {
        if (req.body.timKiem != undefined) {
            console.log(req.body.timKiem);
            let listUser = await User.accountModel.find({ userName: req.body.timKiem });
            console.log(listUser);
            res.render('home/add', { listUser: listUser })
            return
        }
        for (let i = 0; i < listUser.length; i++) {
            if(listUser[i].userName == req.body.userName.trim()){
                error++;
            }
        }
         if(error > 0){
            msg = "Tài khoản đã tồn tại trong hệ thống";
            console.log("Tài khoản đã tồn tại trong hệ thống");
            return res.render('home/add', { listUser: listUser ,msg:msg})
         }
         if(req.body.userName < 5){
            msg = "Tên tài khoản phải 5 lớn hơn kí tự";
            return res.render('home/add', { listUser: listUser ,msg:msg})
         }
         if(req.body.passWord < 5){
            msg = "Mật khẩu phải lớn hơn 5 kí tự";
            return res.render('home/add', { listUser: listUser ,msg:msg})
         }
        try {
            const salt = await bcrypt.genSalt(15);
            const user = new User.accountModel(req.body);
            user.passWord = await bcrypt.hash(req.body.passWord, salt);
            const token = await user.generateAuthToken();
            let new_account = await user.save();
            console.log(new_account);
            msg = "Thêm thành công"
            listUser = await User.accountModel.find();
        } catch (error) {
            msg = "Thêm thất bại" + error.message;
            console.log(error);
        }
    }
    res.render('home/add', { listUser: listUser,msg:msg })
}
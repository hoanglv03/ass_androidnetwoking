const myMD = require("../models/User");
const bcrypt = require("bcrypt");
exports.getDangNhap = async (req, res, next) => {
    let msg = "";
    if (req.method == "POST") {
        try {
            var objU = await myMD.accountModel.findByCredentials(req.body.username, req.body.passWrd)
            console.log(objU);
            if (!objU) {
                msg = "Sai thông tin đăng nhập";
            } else {
                const token = await objU.generateAuthToken()
                req.session.userLogin = objU
                console.log(token);
                return res.redirect("/home/home");
            }

        } catch (error) {
            msg = "Lỗi :" + error.message;
        }
    }
    res.render("home/dangnhap", { msg: msg });
};
exports.register = async (req, res, next) => {
    console.log(req.body);
    let msg = "";
    if (req.method == "POST") {
        try {
            const salt = await bcrypt.genSalt(15);
            const user = new myMD.accountModel(req.body);
            user.passWord = await bcrypt.hash(req.body.passWrd, salt);
            const token = await user.generateAuthToken();
            let new_u = await user.save()
            console.log(new_u);
            msg = "Đăng ký thành công";
        } catch (error) {
            console.log(error);
            msg = "Lỗi" + error.message;
        }
    }
    res.render("home/register", { msg: msg });
};

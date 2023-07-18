exports.yeuCauDangNhap = (req,res,next) =>{
    if(req.session.userLogin){
        next()
    }else{
        return res.redirect('/')
    }
}
exports.khongYecCauDangNhap = (req,res,next) =>{
    if(!req.session.userLogin){
        next()
    }else{
        console.log("đã loginr");
        return res.redirect('/home/home')
    }
}
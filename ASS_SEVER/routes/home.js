var express = require('express');
var router = express.Router();
var homeController =  require('../controller/home')
var spController =  require('../controller/sanpham')

var userController =  require('../controller/user')
var multer = require('multer')
var objUpload = multer({dest:'./tmp'})
var check_login = require('../middlewares/checkLogin')




// router.use( check_login.yeuCauDangNhap);

router.get('/home',spController.list)
router.get('/:id/chitiet',spController.chiTiet)
router.post('/home',objUpload.single('anhsanpham'),spController.add)
router.get('/add',userController.listUser);
router.get('/:id/editUser',userController.edit);
router.post('/:id/editUser',userController.edit);
router.post('/:id/deleteUser',userController.deleteUser);
router.post('/add',userController.listUser);
router.get('/:id/edit',spController.edit)
router.post('/:id/edit',objUpload.single('anhsanpham'),spController.edit)
router.post('/:id',spController.deleteProduct)

router.get('/logout',spController.logOut)

module.exports = router;
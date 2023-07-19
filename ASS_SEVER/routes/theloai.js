var express = require('express');
var router = express.Router();
var spController =  require('../controller/sanpham')
var check_login = require('../middlewares/checkLogin')
// router.use( check_login.yeuCauDangNhap);
router.get('/theLoai',spController.listLoai)
router.post('/theLoai',spController.listLoai)
router.get('/:id/editTheLoai',spController.editTheLoai)
router.post('/:id/editTheLoai',spController.editTheLoai)
router.post('/:id/deleteTheLoai',spController.deleteTheLoai)
module.exports = router;
var express = require('express');
var router = express.Router();
var check_login = require('../middlewares/checkLogin')
/* GET home page. */
var homeCtrl = require('../controller/home')
router.get('/',check_login.khongYecCauDangNhap,homeCtrl.getDangNhap)
router.post('/',homeCtrl.getDangNhap)
router.get('/register',homeCtrl.register)
router.post('/register',homeCtrl.register)
module.exports = router;

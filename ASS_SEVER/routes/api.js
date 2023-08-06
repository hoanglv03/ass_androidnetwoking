var express = require('express');
var router = express.Router();
var apiSever = require('../controller/api/api-sever');
var mdw = require('../middlewares/api.auth')
//Product
router.get('/listProduct'   ,apiSever.listProduct);
router.post('/addProduct',apiSever.addProduct);
router.post('/searchProduct',apiSever.seachProduct);
router.put('/updateProduct/:id',apiSever.updateProduct);
router.delete('/deleteProduct/:id',apiSever.deleteProduct);


//Category

router.get('/listCategory',apiSever.listCategory);
router.post('/addCategory',apiSever.addCategory);
router.put('/updateCategory/:id',apiSever.updateCategory);
router.delete('/deleteCategory/:id',apiSever.deleteCategory);

//User


router.get('/listUser',apiSever.listUser);
router.post('/addUser',apiSever.addUser);
router.put('/updateUser/:id',apiSever.updateUser);
router.delete('/deleteUser/:id',apiSever.deleteUser);
router.put('/changePassword/:id',apiSever.changePassword);
//Login and register user

router.post('/login',apiSever.login)
router.post('/register',apiSever.register)


module.exports = router;
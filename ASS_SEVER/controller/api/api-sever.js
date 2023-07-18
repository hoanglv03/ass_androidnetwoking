var Product = require("../../models/Products");
const bcrypt = require("bcrypt");
var User = require("../../models/User");
var listObject = {
    status:1,
    msg: ''
}
var fs = require("fs");
//Login and Register

exports.login = async (req,res,next) =>{
  
    try {
        var user = await User.accountModel.findByCredentials(req.body.userName, req.body.passWord)
     
        if (!user) {
            return res.status(401)
            .json({error: 'Sai thông tin đăng nhập'})
        } else {
            const token = await user.generateAuthToken()
            //var userSendClient = user.userName
            console.log("Đã chạy vào đây");
            console.log(user,token);
            return res.status(200).send({user, token })
        }
    } catch (error) {
        console.log(error)
        return res.status(500).json({msg: error.message})   
    }
}
exports.register = async (req,res,next) =>{
    try {
        const salt = await bcrypt.genSalt(15);
        const user = new User.accountModel(req.body);
        user.passWord = await bcrypt.hash(req.body.passWord, salt);
        const token = await user.generateAuthToken();
        let new_u = await user.save()
        console.log(new_u);
        return res.status(201).json({ user: new_u, token })

    } catch (error) {
        console.log(error)
        return res.status(500).json({msg: error.message})
 
    }
}


//      --------- PRODUCT -------
exports.listProduct = async (req,res,next) =>{
    const pageSize = 4
    if(req.query.page != null){
        let page = req.query.page;
        page  = parseInt(page);
        let skip = (page -1 )*pageSize;
        let listProducts = await Product.spMododel
        .find()
        .skip(skip)
        .limit(pageSize);
        listObject.data = listProducts;
    }else{
        try {
            let listTL = await Product.loaiSanPhamModel.find();
           var listProducts = await Product.spMododel.find().populate("id_loai", "_id, name")
            
            if(listProducts.length > 0)
            listObject.data = listProducts;
            else{
                listObject.status = 0
                listObject.msg = "Không có dữ liệu"
            }
        } catch (error) {
            listObject.status = 0
            listObject.msg = error.message
        }
    }
    res.json(listObject)
}
exports.seachProduct = async (req,res,next) =>{
    try {
        let listProducts = await Product.spMododel.find({tenSanPham:req.body.tenSanPham}).populate("id_loai", "_id, name");
        if(listProducts.length > 0)
        listObject.data = listProducts;
        else{
            listObject.status = 0
            listObject.msg = "Không có dữ liệu"
        }
    } catch (error) {
        listObject.status = 0
        listObject.msg = error.message
    }
    console.log(listObject);
    res.json(listObject)
}
exports.addProduct = async (req,res,next) =>{
    let newProducts = new Product.spMododel();
    newProducts.anhsanpham = req.body.anhsanpham;
    newProducts.tenSanPham = req.body.tenSanPham;
    newProducts.id_loai = req.body.loaiSanPham;
    newProducts.giaTien = req.body.giaTien;
    newProducts.soLuong = req.body.soLuong;
    newProducts.moTa = req.body.moTa;

    console.log(req.body);
    try {
      // di chuyển file từ thư mục  tmp sang public /upload
      //fs.rename(đường dẫn gốc,đường dẫn mới,callback) 
      var new_sp = await newProducts.save();
      console.log(new_sp);
      msg = "thêm thành công";
    } catch (err) {
      msg = "thêm thất bại" + err.message;
      console.log(err);
    }
    res.json(new_sp)
}
exports.updateProduct = async (req,res,next) =>{
    let newProducts = new Product.spMododel();
    let id_product = req.params.id;
    newProducts.anhsanpham = req.body.anhsanpham;
    newProducts.tenSanPham = req.body.tenSanPham;
    newProducts.id_loai = req.body.loaiSanPham;
    newProducts.giaTien = req.body.giaTien;
    newProducts.soLuong = req.body.soLuong;
    newProducts.moTa = req.body.moTa;
    newProducts._id = id_product;
    console.log(req.body);
    try {
      // di chuyển file từ thư mục  tmp sang public /upload
      //fs.rename(đường dẫn gốc,đường dẫn mới,callback) 
     var new_sp = await Product.spMododel.findByIdAndUpdate(
        { _id: id_product },
        newProducts
      );
      console.log(new_sp);
      msg = "thêm thành công";
    } catch (err) {
      msg = "thêm thất bại" + err.message;
      console.log(err);
    }
    res.json(new_sp)
}
exports.deleteProduct = async (req,res,next) =>{
    let deleteProduct = await Product.spMododel.deleteOne({ _id: req.params.id });
    res.json(deleteProduct)
}
//      --------- END -------

// --------Category---------
exports.listCategory = async (req,res,next) =>{
    try {
        let listCategory = await Product.loaiSanPhamModel.find();
        if(listCategory.length > 0)
        listObject.data = listCategory;
        else{
            listObject.status = 0
            listObject.msg = "Không có dữ liệu"
        }
    } catch (error) {
        listObject.status = 0
        listObject.msg = error.message
    }

    res.json(listObject)
}
exports.addCategory = async (req,res,next) =>{
    let newTheLoai = new Product.loaiSanPhamModel();
    newTheLoai.name = req.body.tenloai;
    try {
      var newTheLoai2 = await newTheLoai.save();
      console.log(new_loai);
      msg = "Sửa thành công";
    } catch (error) {
      msg = "Lỗi " + error.message;
      console.log(error);
    }
    res.json(newTheLoai2)
}
exports.updateCategory = async (req,res,next) =>{
    let id_theLoai = req.params.id;
    let newTheLoai = new Product.loaiSanPhamModel();
    newTheLoai._id = id_theLoai;
    newTheLoai.name = req.body.tenloai;
    try {
       var editLoai = await Product.loaiSanPhamModel.findByIdAndUpdate(
          { _id: id_theLoai },
          newTheLoai
        );;
      } catch (error) {
        msg = "Lỗi " + error.message;
        console.log(error);
      }
    res.json(editLoai)
}
exports.deleteCategory = async (req,res,next) =>{
    var deleteU = await Product.loaiSanPhamModel.deleteOne({ _id: req.params.id });
    res.json(deleteU)
}
//      --------- END -------


// --------User---------
exports.listUser = async (req,res,next) =>{
    try {
        let listUser = await User.accountModel.find();
        if(listUser.length > 0)
        listObject.data = listUser;
        else{
            listObject.status = 0
            listObject.msg = "Không có dữ liệu"
            listObject.data = []
        }
    } catch (error) {
        listObject.status = 0
        listObject.msg = error.message
    }

    res.json(listObject)
}
exports.addUser = async (req,res,next) =>{
    const user = new User.accountModel(req.body);
    let new_account = await user.save()
    res.json(new_account)
}
exports.updateUser = async (req,res,next) =>{
    let idUser = req.params.id
    let newUser = new User.accountModel();
    newUser._id = idUser;
    newUser.userName = req.body.userName
    newUser.passWord = req.body.passWord
    newUser.vaiTro = req.body.vaiTro;
   var updateU = await User.accountModel.findByIdAndUpdate({ _id: idUser }, newUser)
    res.json(updateU)
}
exports.deleteUser = async (req,res,next) =>{
   var deleteU = await User.accountModel.deleteOne({ _id: req.params.id })
    res.json(deleteU)
}
//      --------- END -------
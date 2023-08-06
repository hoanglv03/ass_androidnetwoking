var Product = require("../../models/Products");
const bcrypt = require("bcrypt");
var User = require("../../models/User");
var listObject = {
  status: 1,
  msg: "",
};
var fs = require("fs");
//Login and Register

exports.login = async (req, res, next) => {
  const { user } = req.body;
  let results = "";
  try {
    const { users, message, result } =
      await User.accountModel.findByCredentials(user.email, user.passWord, res);

    if (!user) {
      return res.status(401).json({ user: users, message, result });
    } else {
      // await users.generateAuthToken()
      return res.status(200).json({ user: users, message, result });
    }
  } catch (error) {
    console.log(error);
    results = "failure";
    return res
      .status(500)
      .json({ user: {}, message: "Sai thông tin đăng nhập", result: results });
  }
};
exports.changePassword = async (req, res) => {
  const { oldPassword, passWord } = req.body;
  const idUser = req.params.id;
  try {
    const user = await User.accountModel.findById({ _id: idUser });
    const isPasswordMatch = await bcrypt.compare(oldPassword, user.passWord);
    if (!isPasswordMatch) {
      return res.status(500).json({ msg: "Sai mật khẩu" });
    }
    const salt = await bcrypt.genSalt(15);
    user.passWord = await bcrypt.hash(passWord, salt);
    await user.generateAuthToken();
    user.id = idUser;
    await User.accountModel.findByIdAndUpdate({ _id: idUser }, user);
    console.log("Đổi thành công", req.body, idUser);
    return res.status(200).json(user);
  } catch (error) {
    console.log(error);
    return res.status(500).json({ msg: error.message });
  }
};
exports.register = async (req, res, next) => {
  const { user } = req.body;
  console.log(req.body);
  try {
    const salt = await bcrypt.genSalt(15);
    const userModal = new User.accountModel(user);
    userModal.passWord = await bcrypt.hash(user.passWord, salt);
    await userModal.generateAuthToken();
    let new_u = await userModal.save();
    console.log(new_u);
    return res.status(201).json({
      user: userModal,
      message: "Đăng ký tài khoản thành công",
      result: "success",
    });
  } catch (error) {
    console.log(error);
    return res.status(500).json({
      user: userModal,
      message: "Đăng ký thất bại",
      result: "failure",
    });
  }
};

//      --------- PRODUCT -------
exports.listProduct = async (req, res, next) => {
  const pageSize = 4;
  if (req.query.page != null) {
    let page = req.query.page;
    page = parseInt(page);
    let skip = (page - 1) * pageSize;
    let listProducts = await Product.spMododel
      .find()
      .skip(skip)
      .limit(pageSize);
    listObject.data = listProducts;
  } else {
    try {
      let listTL = await Product.loaiSanPhamModel.find();
      var listProducts = await Product.spMododel
        .find()
        .populate("id_loai", "_id, name");

      if (listProducts.length > 0) listObject.data = listProducts;
      else {
        listObject.status = 0;
        listObject.msg = "Không có dữ liệu";
      }
    } catch (error) {
      listObject.status = 0;
      listObject.msg = error.message;
    }
  }

  res.json(listObject.data);
};
exports.seachProduct = async (req, res, next) => {
  const { product } = req.body;
  console.log("body", req.body);
  let listProducts = null;
  try {
   listProducts = await Product.spMododel.find({ tenSanPham: product.tenSanPham })
    if (listProducts.length > 0) listObject.product = listProducts;
    else {
      listObject.result = 'success';
      listObject.message = "Không có dữ liệu";
    }
  } catch (error) {
    listObject.result = 'failure';
    listObject.message = error.message;
  }
  console.log(listProducts);
  res.json(listProducts);
};
exports.addProduct = async (req, res, next) => {
  let newProducts = new Product.spMododel();
  const { product } = req.body;
  console.log(req.file);
  newProducts.tenSanPham = product.tenSanPham;
  newProducts.giaTien = product.giaTien;
  newProducts.soLuong = product.soLuong;
  console.log(req.body);
  let result = "";
  try {
    var new_sp = await newProducts.save();
    console.log(new_sp);
    msg = "thêm thành công";
    result = "success";
  } catch (err) {
    msg = "thêm thất bại" + err.message;
    result = "failure";
    console.log(err);
  }
  res.json({ product: new_sp, message: msg, result: result });
};
exports.updateProduct = async (req, res, next) => {
  let newProducts = new Product.spMododel();
  let id_product = req.params.id;
  const { product } = req.body;
  newProducts.tenSanPham = product.tenSanPham;
  newProducts.giaTien = product.giaTien;
  newProducts.soLuong = product.soLuong;
  newProducts._id = id_product;
  console.log(req.body);
  let result = "";
  try {
    var productEdit = await Product.spMododel.findByIdAndUpdate(
      { _id: id_product },
      newProducts
    );
    result = "success";
    productEdit = newProducts;
    msg = "Sửa thành công";
  } catch (err) {
    result = "failure";
    msg = "Sửa thất bại" + err.message;
    console.log(err);
  }
  res.json({ product: productEdit, message: msg, result: result });
};
exports.deleteProduct = async (req, res, next) => {
  let deleteProduct = await Product.spMododel.deleteOne({ _id: req.params.id });
  res.json(deleteProduct);
};
//      --------- END -------

// --------Category---------
exports.listCategory = async (req, res, next) => {
  try {
    let listCategory = await Product.loaiSanPhamModel.find();
    if (listCategory.length > 0) listObject.data = listCategory;
    else {
      listObject.status = 0;
      listObject.msg = "Không có dữ liệu";
    }
  } catch (error) {
    listObject.status = 0;
    listObject.msg = error.message;
  }

  res.json(listObject);
};
exports.addCategory = async (req, res, next) => {
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
  res.json(newTheLoai2);
};
exports.updateCategory = async (req, res, next) => {
  let id_theLoai = req.params.id;
  let newTheLoai = new Product.loaiSanPhamModel();
  newTheLoai._id = id_theLoai;
  newTheLoai.name = req.body.tenloai;
  try {
    var editLoai = await Product.loaiSanPhamModel.findByIdAndUpdate(
      { _id: id_theLoai },
      newTheLoai
    );
  } catch (error) {
    msg = "Lỗi " + error.message;
    console.log(error);
  }
  res.json(editLoai);
};
exports.deleteCategory = async (req, res, next) => {
  var deleteU = await Product.loaiSanPhamModel.deleteOne({
    _id: req.params.id,
  });
  res.json(deleteU);
};
//      --------- END -------

// --------User---------
exports.listUser = async (req, res, next) => {
  try {
    let listUser = await User.accountModel.find();
    if (listUser.length > 0) listObject.data = listUser;
    else {
      listObject.status = 0;
      listObject.msg = "Không có dữ liệu";
      listObject.data = [];
    }
  } catch (error) {
    listObject.status = 0;
    listObject.msg = error.message;
  }

  res.json(listObject);
};
exports.addUser = async (req, res, next) => {
  const user = new User.accountModel(req.body);
  let new_account = await user.save();
  res.json(new_account);
};
exports.updateUser = async (req, res, next) => {
  let idUser = req.params.id;
  let newUser = new User.accountModel();
  newUser._id = idUser;
  newUser.userName = req.body.userName;
  newUser.passWord = req.body.passWord;
  newUser.vaiTro = req.body.vaiTro;
  var updateU = await User.accountModel.findByIdAndUpdate(
    { _id: idUser },
    newUser
  );
  res.json(updateU);
};
exports.deleteUser = async (req, res, next) => {
  var deleteU = await User.accountModel.deleteOne({ _id: req.params.id });
  res.json(deleteU);
};
//      --------- END -------

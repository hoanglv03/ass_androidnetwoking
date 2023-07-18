var fs = require("fs");
var Product = require("../models/Products");
const pageSize = 4;
exports.list = async (req, res, next) => {
 
  let msg = "";
  const sortType =
    req.query.column === res.locals._sort.column
      ? res.locals._sort.type
      : "default";

  const icons = {
    default: "bx bxs-sort-alt",
    esc: "bx bx-sort-down",
    desc: "bx bx-sort-up",
  };
  const types = {
    default: "desc",
    esc: "desc",
    desc: "esc",
  };
  const icon = icons[sortType];
  const type = types[sortType];

  let listTL = await Product.loaiSanPhamModel.find();
  var product = await Product.spMododel.find().populate("id_loai", "_id, name").limit(pageSize);
  if (req.query.hasOwnProperty("_sort")) {
    console.log("đã vào");
    product = await Product.spMododel
      .find()
      .populate("id_loai", "_id, name")
      .sort({
        [req.query.column]: req.query.type == "desc" ? "-1" : "1",
      }).limit(pageSize);
  }
  if (req.query.page != null) {
    let page = req.query.page;
    page = parseInt(page);
    let skip = (page - 1) * pageSize;
    product = await Product.spMododel
      .find()
      .skip(skip)
      .limit(pageSize)
      .populate("id_loai", "_id, name");
    
  }
  res.render("home/home", {
    listSanPham: product,
    listTL: listTL,
    icon: icon,
    type: type,
    msg: msg,
  });
};
exports.update = (req, res, next) => {
  res.json(req.params.id);
  console.log(req.body);
  console.log(req.file);
  console.log(req.params.id);
};
exports.chiTiet = async (req, res, next) => {
  let id_product = req.params.id;
  console.log(id_product);
  var listTL = await Product.loaiSanPhamModel.find();
  var product = await Product.spMododel
    .findById(id_product)
    .populate("id_loai", "_id, name");
  res.render("home/chiTiet", { product: product, listTL: listTL });
};

exports.edit = async (req, res, next) => {
  console.log(req.body);
  console.log(req.file);
  let msg = "";
  let id_product = req.params.id;
  try {
    var listTL = await Product.loaiSanPhamModel.find();
    var product = await Product.spMododel.findById(id_product);
  } catch (error) {
    msg = "Lỗi" + error.message;
  }
  if (req.method == "POST") {
    let newProducts = new Product.spMododel();
    if (req.file != undefined) {
      newProducts.anhsanpham = req.file.originalname;
    } else {
      newProducts.anhsanpham = req.body.anhsanphamcu;
    }
    newProducts._id = id_product;
    newProducts.tenSanPham = req.body.tenSanPham.trim();
    newProducts.id_loai = req.body.loaiSanPham;
    newProducts.giaTien = req.body.giaTien.trim();
    newProducts.soLuong = req.body.soLuong.trim();
    newProducts.moTa = req.body.moTa.trim();
    console.log(newProducts);
    try {
      // di chuyển file từ thư mục  tmp sang public /upload
      //fs.rename(đường dẫn gốc,đường dẫn mới,callback)
      if (req.file != undefined) {
        fs.rename(
          req.file.path,
          "./public/upload/" + req.file.originalname,
          (err) => {
            if (err) {
              console.log(err);
            } else {
              console.log(
                "URL : http://localhost:3000" + req.file.originalname
              );
            }
          }
        );
      }
      await Product.spMododel.findByIdAndUpdate(
        { _id: id_product },
        newProducts
      );
      console.log("Sửa thành công");
      msg = "sửa thành công";
    } catch (err) {
      msg = "thêm thất bại" + err.message;
      console.log(err);
    }
  }
  res.render("home/editSp", { product: product, listTL: listTL, msg: msg });
  console.log(req.params.id);
};
exports.deleteProduct = async (req, res, next) => {
  await Product.spMododel.deleteOne({ _id: req.params.id });
  console.log("đã vào delete");
  res.redirect("back");
};
exports.deleteTheLoai = async (req, res, next) => {
  await Product.loaiSanPhamModel.deleteOne({ _id: req.params.id });
  console.log("đã vào delete");
  res.redirect("back");
};
exports.add = async (req, res, next) => {
  var msg = "";
  const sortType =
    req.query.column === res.locals._sort.column
      ? res.locals._sort.type
      : "default";

  const icons = {
    default: "bx bxs-sort-alt",
    esc: "bx bx-sort-down",
    desc: "bx bx-sort-up",
  };
  const types = {
    default: "desc",
    esc: "desc",
    desc: "esc",
  };
  const icon = icons[sortType];
  const type = types[sortType];

  var product = await Product.spMododel.find().populate("id_loai", "_id, name");
  if (req.query.hasOwnProperty("_sort")) {
    console.log("đã vào");
    product = await Product.spMododel
      .find()
      .populate("id_loai", "_id, name")
      .sort({
        [req.query.column]: req.query.type == "desc" ? "-1" : "1",
      }).limit(pageSize);
  }
  console.log(req.body);
  if (req.method == "POST") {
    if (req.body.locLoaiSanPham != null) {
      let id_loaiSanPham = { id_loai: req.body.locLoaiSanPham };
      if (req.body.locLoaiSanPham == "all") {
        id_loaiSanPham = null;
      }

      console.log("đã chạy vào");
      let listTL = await Product.loaiSanPhamModel.find();
      var product = await Product.spMododel
        .find(id_loaiSanPham)
        .populate("id_loai", "_id, name").limit(pageSize);
      res.render("home/home", {
        listSanPham: product,
        listTL: listTL,
        icon: icon,
        type: type,
        msg: msg,
      });
      return;
    }
    //  check đầu vào

    let newProducts = new Product.spMododel();
    newProducts.anhsanpham = req.file.originalname;
    newProducts.tenSanPham = req.body.tenSanPham.trim();
    newProducts.id_loai = req.body.loaiSanPham;
    newProducts.giaTien = req.body.giaTien.trim();
    newProducts.soLuong = req.body.soLuong.trim();
    newProducts.moTa = req.body.moTa.trim();

    console.log(req.body);
    try {
      // di chuyển file từ thư mục  tmp sang public /upload
      //fs.rename(đường dẫn gốc,đường dẫn mới,callback)
      fs.rename(
        req.file.path,
        "./public/upload/" + req.file.originalname,
        (err) => {
          if (err) {
            console.log(err);
          } else {
            console.log("URL : http://localhost:3000" + req.file.originalname);
          }
        }
      );
      let new_sp = await newProducts.save();
      console.log(new_sp);
      msg = "thêm thành công";
    } catch (err) {
      msg = "thêm thất bại" + err.message;
      console.log(err);
    }
  }
  let listTL = await Product.loaiSanPhamModel.find();
  var product = await Product.spMododel.find().populate("id_loai", "_id, name").limit(pageSize);
  res.render("home/home", {
    listSanPham: product,
    listTL: listTL,
    icon: icon,
    type: type,
    msg: msg,
  });
};

exports.listLoai = async (req, res, next) => {
  let msg = "";
  console.log(req.body);
  if (req.method == "POST") {
    let newTheLoai = new Product.loaiSanPhamModel();
    newTheLoai.name = req.body.tenloai;
    try {
      await newTheLoai.save();
      console.log(new_loai);
      msg = "Sửa thành công";
    } catch (error) {
      msg = "Lỗi " + error.message;
      console.log(error);
    }
  }
  let listTL = await Product.loaiSanPhamModel.find();
  res.render("home/theLoai", { listTL: listTL });
};
exports.editTheLoai = async (req, res, next) => {
  let id_theLoai = req.params.id;
  if (req.method == "POST") {
    let newTheLoai = new Product.loaiSanPhamModel();
    newTheLoai._id = id_theLoai;
    newTheLoai.name = req.body.tenloai.trim();
    try {
      await Product.loaiSanPhamModel.findByIdAndUpdate(
        { _id: id_theLoai },
        newTheLoai
      );
      console.log(new_loai);
      msg = "Sửa thành công";
    } catch (error) {
      msg = "Lỗi " + error.message;
      console.log(error);
    }
  }

  let listTL = await Product.loaiSanPhamModel.findById(id_theLoai);
  res.render("home/editTheLoai", { listTL: listTL });
};
exports.logOut = async (req, res, next) => {
  if (req.session != null)
    req.session.destroy(function () {
      console.log("Đăng xuất thành công");
      res.redirect("/");
    });
};

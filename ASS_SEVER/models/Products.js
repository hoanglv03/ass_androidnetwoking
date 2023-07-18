var db = require('../config/db');
const Products = new db.mongoose.Schema(
  {
    anhsanpham: { type: String },
    tenSanPham: { type: String },
    id_loai: {
      type: db.mongoose.Schema.Types.ObjectId,
      ref: "loaiSanPhamModel",
    },
    giaTien: { type: String },
    soLuong: { type: String },
    moTa: { type: String },
    // ngoài ra có thể thêm thời gian mặc định như default: Date.now
  },
  { collection: "products" }
);
const loaiSanPham = new db.mongoose.Schema(
  {
    name: { type: String, require: true },
  },
  { collection: "the_loai" }  
);

let spMododel = db.mongoose.model("products", Products);
let loaiSanPhamModel = db.mongoose.model("loaiSanPhamModel", loaiSanPham);
module.exports = { spMododel, loaiSanPhamModel };

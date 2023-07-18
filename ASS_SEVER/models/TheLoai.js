var db = require('../config/db');
const loaiSanPham = new db.mongoose.Schema(
  {
    name: { type: String, require: true },
  },
  { collection: "the_loai" }  
);

let loaiSanPhamModel = db.mongoose.model("loaiSanPhamModel", loaiSanPham);
module.exports = {loaiSanPhamModel}
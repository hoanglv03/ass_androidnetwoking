var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var methodOverride = require('method-override')
var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');
var bodyParser = require('body-parser')
var session = require('express-session')
var SortMiddleware = require('./middlewares/SortMiddleware')
var home = require('./routes/home')
var theloai = require('./routes/theloai')
var apiRouter = require('./routes/api')
var app = express();
// cách kết nối với db
var db = require('./config/db');
require('dotenv').config();
app.use(bodyParser.urlencoded({ extended: false }))

// parse application/json
app.use(bodyParser.json())
// đổi method nhận từ form


//connect to db

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use(SortMiddleware)

app.use(logger('dev'));
app.use(express.json());

app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));
app.use(methodOverride('_method'))
app.use(session({
  secret:process.env.KEY_SESSION, // chuỗi ký tự đặc biệt để Session mã hóa, tự viết
  resave:true,
  saveUninitialized:true
 }));
 
app.use('/', indexRouter);
app.use('/users', usersRouter);
app.use('/home',home)
app.use('/theloai',theloai)
app.use('/api',apiRouter)
// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});


// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  if(req.originalUrl.indexOf('/api') == 0){
    //đang truy cập vào link api
    res.json({
      status:0,
      msg: err.message
    })
  }else{

    res.render('error');
  }

});

module.exports = app;

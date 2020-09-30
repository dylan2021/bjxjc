Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

function formatduration(kept){
      if(kept < 60){
        kept = kept + '秒';
      }
      else if(kept < 3600){
        kept = parseInt(kept/60) + '分钟' + (kept - parseInt(kept/60) * 60) + '秒';
      }
      else if(kept < 3600 * 24){
        kept = parseInt(kept/3600) + '小时' + parseInt((kept - parseInt(kept/3600) * 3600)/60) + '分钟';
      }
      else{
        kept = parseInt(kept/3600/24) + '天' + parseInt((kept - parseInt(kept/3600/24) * 3600 * 24)/3600) + '小时';
      }
      return kept;
}
/**
 * 	使用方式
 * 	1. 挂载在main.js
 * import apiRequest from '@/request.js'
 * Vue.prototype.apiRequest = apiRequest
 * */

/*
options = {
	data = data // 请求参数
	url = 'api/aaaa',
	method = 'GET | POST'  // 不传递默认POST
}
*/
let apiUrl = 'http://127.0.0.1:8776'

function apiRequest(options) {
	let headers = {}
	var userInfo = uni.getStorageSync('user')
	var token = ''
	if (userInfo) {
		token = userInfo.token
		headers['token'] = token
	}


	/*同步化*/
	return new Promise((res, rej) => {

			uni.request({
				url: apiUrl + options.url,
				method: options.method || 'GET',
				data: options.data,
				header: headers,
				success(data) {
					if (data.data.code === 200) {
						res(data)
					}
					if (data.data.code === 401) {
						returnToLogin()
					}
					if (data.data.code === 402) {
						show(data.data.message, 2000)
					}
					if (data.data.code === 500) {
						show(data.data.message, 2000)
					}

				},
				fail() {
					show('请求超时', 2000)
				}
			})


	})
}

function returnToLogin() {
	console.log("跳转开始")
	uni.showToast({
		icon: 'error',
		title: '请登录'
	});
	setTimeout(function() {
		uni.redirectTo({
			url: '/pages/login/login',
		});
	}, 3000);
}

function show(msg, timeout) {
	uni.showToast({
		icon: 'error',
		title: msg
	});

	setTimeout(function() {
		uni.hideToast()
	}, timeout)
}
export default apiRequest

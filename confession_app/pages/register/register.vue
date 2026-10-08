<template>
	<view>
		<form @submit="register">
			<view class="cu-form-group margin-top">
				<view class="title">姓名</view>
				<input placeholder="请输入您的姓名" name="name"></input>
			</view>
			<view class="cu-form-group">
				<view class="title">登录账号</view>
				<input placeholder="请输入您的登录账号" name="number"></input>
				
			</view>
			<view class="cu-form-group">
				<view class="title">登录密码</view>
				<input placeholder="请输入您的登录密码" name="password" type="password"></input>
				<input hidden name="image" :value="image"></input>
			</view>
			<view class="cu-bar bg-white margin-top">
				<view class="action">
					头像（可不选）
				</view>
				<view class="action">
					{{imgList.length}}/1
				</view>
			</view>
			<view class="cu-form-group">
				<view class="grid col-4 grid-square flex-sub">
					<view class="bg-img" v-for="(item,index) in imgList" :key="index" @tap="viewImage"
						:data-url="imgList[index]">
						<image :src="imgList[index]" mode="aspectFill"></image>
						<view class="cu-tag bg-red" @tap.stop="delImg" :data-index="index">
							<text class='cuIcon-close'></text>
						</view>
					</view>
					<view class="solids" @tap="chooseImage" v-if="imgList.length<1">
						<text class='cuIcon-cameraadd'></text>
					</view>
				</view>
			</view>
			<view class="padding flex flex-direction">
				<button form-type="submit" class="cu-btn bg-cyan lg">注册</button>
			</view>
		</form>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				imgList: [],
				image:"",
				
			}
		},
		methods: {
			chooseImage() {
				var that = this
				uni.chooseImage({
					count: 1, //默认9
					sizeType: ['original', 'compressed'],
					sourceType: ['album'], //从相册选择
					success: (res) => {
						that.imgList = res.tempFilePaths
						uni.uploadFile({
							url: 'http://127.0.0.1:8776' + '/api/upload/upload', //仅为示例，非真实的接口地址
							filePath: res.tempFilePaths[0],
							name: 'image',
							success: (uploadFileRes) => {
								that.image = JSON.parse(uploadFileRes.data).data
							}
						});
					}
				});
			},
			viewImage(e) {
				uni.previewImage({
					urls: this.imgList,
					current: e.currentTarget.dataset.url
				});
			},
			delImg(e) {
				var that = this
				uni.showModal({
					title: that.customerName,
					content: '确定要删除吗？',
					cancelText: '在想想',
					confirmText: '确定',
					success: res => {
						if (res.confirm) {
							that.imgList = []
						}
					}
				})
			},
			//确认注册，跳转到登录页面
			register(e) {
				var that = this
				console.log(e)
				//有些采用得data接收数据，手写data参数
				//请求注册接口
				if(e.detail.value.name===''||e.detail.value.number===''||e.detail.value.password===''){
					uni.showToast({
						icon: 'none',
						title: '请输入完整'
					});
					return;
				}
				if (e.detail.value.number.length < 2) {
					uni.showToast({
						icon: 'none',
						title: '账号最短为 2 个字符'
					});
					return;
				}
				if (e.detail.value.password.length < 2) {
					uni.showToast({
						icon: 'none',
						title: '密码最短为 2 个字符'
					});
					return;
				}
				that.$apiRequest({
						url: '/api/user/register',
						method: 'POST',
						data: {
							"image":this.image,
							"name":e.detail.value.name,
							"number":e.detail.value.number,
							"password":e.detail.value.password
						}
					})
					.then(res => {
						uni.navigateTo({
							url: '../login/login'
						});
					})
			}
		}
	}
</script>

<style>

</style>

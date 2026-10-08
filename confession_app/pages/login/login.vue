<template>
	<view class="container">
		<view class="  flex-wrap padding ">
			<form @submit="login">
				<view class="cu-form-group margin-top">
					<view class="title">账号</view>
					<input placeholder="请输入账号" name="userName"></input>
				</view>
				<view class="cu-form-group margin-top">
					<view class="title">密码</view>
					<input required="required" type="password" placeholder="请输入密码" name="userPwd"></input>
				</view>
				<view class="padding flex flex-direction">
					<button form-type="submit" class="cu-btn bg-cyan lg">登录</button>
				</view>
			</form>
			<view class="padding flex flex-direction">
				<button form-type="submit" class="cu-btn bg-cyan lg" @click="register()">注册</button>
			</view>
			<!--copyRight-->
			<view class="foot">{{copyRight}}</view>

			<!--加载框-->
			<view class="cu-load load-modal" v-if="loadModal">
				<!-- <view class="cuIcon-emojifill text-orange"></view> -->
				<image src="/static/logo.png" mode="aspectFit"></image>
				<view class="gray-text">登录中...</view>
			</view>
			<!--模态弹窗-->
			<view class="cu-modal" :class="modalName=='loginModal'?'show':''">
				<view class="cu-dialog">
					<view class="cu-bar bg-white justify-end">
						<view class="text-pink text-lg content">警告！</view>
						<view class="action" @tap="hideModal">
							<text class="cuIcon-close text-red"></text>
						</view>
					</view>
					<view class="padding-xl">
						{{msg}}
					</view>
				</view>
			</view>
		</view>
	</view>

</template>

<script>
	export default {
		data() {
			return {
				loadModal: false,
				userName: '',
				userPwd: '',
				modalName: null,
				msg: '',
				copyRight: ''
			}
		},
		methods: {
			//跳转到注册页面
			register(e) {
				uni.navigateTo({
					url: '../register/register'
				});
			},
			login(e) {
				var formdata = e.detail.value
				var number = formdata.userName
				var password = formdata.userPwd
				if (number.length < 2) {
					uni.showToast({
						icon: 'none',
						title: '账号最短为 2 个字符'
					});
					return;
				}
				if (password.length < 2) {
					uni.showToast({
						icon: 'none',
						title: '密码最短为 2 个字符'
					});
					return;
				}

				this.$apiRequest({
						url: '/api/user/login',
						method: 'POST',
						data: {
							"number":number,
							"password":password
						}
					})
					.then(res => {
						// 保存token到缓存
						uni.setStorageSync('user', res.data.data);
						uni.switchTab({
							url: '../confess/confess'
						});

					})
			},
			// 隐藏模态
			hideModal(e) {
				this.modalName = null
			}
		},
		onLoad() {
			var year = 'Copyright © ' + (new Date().getFullYear()) + ' copyPast'
			this.copyRight = year

		}
	}
</script>

<style>
	.cu-form-group .title {
		min-width: calc(4em + 15px);
	}

	.forms {
		/* top: 80upx; */
	}

	.foot {
		position: fixed;
		background-color: rgba(43, 166, 166, 0.5);
		color: #FFF;
		bottom: 0;
		left: 0;
		right: 0;
		text-align: center;
		height: 20px;
		line-height: 20px;
	}
</style>

<template>
	<view>
		<!--表单开始-->
		<form @submit="addConfess">
			<view class="cu-form-group margin-top">
				<view class="title">标题</view>
				<input placeholder="输入标题" name="title"></input>
			</view>
			<view class="cu-form-group" v-if="type===1 || type===2">
				<view class="title">物品</view>
				<input placeholder="输入物品名" name="thing"></input>
			</view>
			<!--物品类型-->
			<view v-if="type===2">
				<view class="cu-form-group">
					<view class="title">类型</view>
					<view>
						<radio-group class="block" @change="thingsRadioChange">
							<radio class='blue radio' :class="thingsRadio=='S'?'checked':''"
								:checked="thingsRadio=='S'?true:false" value="S">拾取</radio>
							<radio class='blue radio' :class="thingsRadio=='D'?'checked':''"
								:checked="thingsRadio=='D'?true:false" value="D">丢失</radio>
						</radio-group>
					</view>
				</view>
				<view class="cu-form-group">
					<view class="title">拾取/丢失地点</view>
					<input placeholder="请输入地点" name="address"></input>
				</view>
			</view>
			<view class="cu-form-group align-start" v-if="type===1 || type===0 || type===3">
				<textarea maxlength="-1" @input="textareaInput" placeholder="闲置描述" v-if="type===1"></textarea>
				<textarea maxlength="-1" @input="textareaInput" placeholder="表白或吐槽内容"
					v-if="type===0 || type===3"></textarea>
			</view>
			<view class="cu-form-group" v-if="type===1">
				<view class="title">开个价</view>
				<input placeholder="开个价/元" name="price"></input>
				<text class='cuIcon-moneybagfill text-orange'></text>
			</view>
			<view class="cu-form-group margin-top" v-if="type===1">
				<view class="title">选择分类</view>
				<picker @change="pickerChange" :value="pickerIndex" :range="picker">
					<view class="picker">
						{{picker[pickerIndex]}}
					</view>
				</picker>
			</view>
			<view v-if="type!=3">
				<view class="cu-form-group">
					<view class="title">联系方式</view>
					<view>
						<radio-group class="block" @change="radioChange">
							<radio class='blue radio' :class="connect=='0'?'checked':''" :checked="connect=='0'?true:false"
								value="0">手机号</radio>
							<radio class='blue radio' :class="connect=='1'?'checked':''" :checked="connect=='1'?true:false"
								value="1">微信</radio>
							<radio class='blue radio' :class="connect=='2'?'checked':''" :checked="connect=='2'?true:false"
								value="2">QQ</radio>
						</radio-group>
					</view>
				</view>
				<view class="cu-form-group margin-top">
					<input placeholder="联系方式输入,若有其他联系方式,请输入在备注中" name="elseConnect"></input>
				</view>
			</view>
			<view class="cu-form-group align-start" v-if="type!=3">
				<textarea maxlength="-1" @input="textareaInputRemark" placeholder="请输入备注"></textarea>
			</view>
			<!--图片上传-->
			<view class="cu-bar bg-white margin-top">
				<view class="action">
					图片上传
				</view>
				<view class="action">
					{{imgList.length}}/2
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
					<view class="solids" @tap="chooseImage" v-if="imgList.length<2">
						<text class='cuIcon-cameraadd'></text>
					</view>
				</view>
			</view>
			<view class="padding flex flex-direction">
				<button form-type="submit" class="cu-btn bg-cyan lg">发布</button>
			</view>
		</form>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				type: Number,
				info: String,
				//0手机1微信2qq
				connect: '0',
				remark: String,
				picker: ['数码', '衣物', '书籍', '化妆品', '其他'],
				pickerIndex: 0,
				//图片
				imgList: [],
				thingsPickerIndex: 0,
				//s 拾取 D 丢失
				thingsRadio: 'S'
			}
		},
		onLoad(val) {
			let e = parseInt(val.type)
			this.type = e
			let title = e === 0 ? '发布表白' : (e === 1 ? '发布闲置' : (e === 2 ? '发布寻物' : '发布吐槽'))
			uni.setNavigationBarTitle({
				title: title
			})
		},
		methods: {
			textareaInput(e) {
				this.info = e.detail.value
			},
			textareaInputRemark(e) {
				this.remark = e.detail.value
			},
			radioChange(e) {
				this.connect = e.detail.value
			},
			pickerChange(e) {
				this.index = e.detail.value
			},
			thingsRadioChange(e) {
				this.thingsRadio = e.detail.value
			},
			chooseImage() {
				uni.chooseImage({
					count: 2, 
					sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
					sourceType: ['album'], //从相册选择
					success: (res) => {
						uni.uploadFile({
							url: 'http://127.0.0.1:8776' + '/api/upload/upload', //仅为示例，非真实的接口地址
							filePath: res.tempFilePaths[0],
							name: 'image',
							success: (uploadFileRes) => {
								// that.imgList = JSON.parse(uploadFileRes.data).data
								if (this.imgList.length != 0) {
									this.imgList = this.imgList.concat(JSON.parse(uploadFileRes.data).data)
								} else {
									this.imgList.push(JSON.parse(uploadFileRes.data).data)
								}
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
				uni.showModal({
					title: '提醒',
					content: '是否删除照片？',
					cancelText: '再看看',
					confirmText: '再见',
					success: res => {
						if (res.confirm) {
							this.imgList.splice(e.currentTarget.dataset.index, 1)
						}
					}
				})
			},
			addConfess(e) {
				console.log(e)
				if (e.detail.value.title == "" ) {
					uni.showToast({
						icon: 'none',
						title: '请输入完整'
					});
					return;
				}
				//构造obj
				let map = {}
				let confess = {}
				confess.title = e.detail.value.title
				confess.connect = this.connect
				confess.elseConnect=e.detail.value.elseConnect
				confess.type=this.type
				confess.imgList=this.imgList
				map.remark=this.remark
				//构造map
				if(this.type===0 ||  this.type===3){
					map.info=this.info
				}
				if(this.type===0){
					map.remark=this.remark
				}
				if(this.type===1){
					map.info=this.info
					map.thing=e.detail.value.thing
					map.price=e.detail.value.price
					map.pickerIndex=this.pickerIndex
					map.remark=this.remark
				}
				if(this.type===2){
					map.thing=e.detail.value.thing
					//类型
					map.thingsRadio=this.thingsRadio
					map.address=e.detail.value.address
					map.remark=this.remark
				}
				confess.map=map
				this.$apiRequest({
						url: '/api/confess/creatConfess',
						method: 'POST',
						data: JSON.stringify(confess)
					})
					.then(res => {
						//回到首页
						uni.switchTab({
							url:"../confess/confess"
						})
					})
			}
		}
	}
</script>

<style>

</style>

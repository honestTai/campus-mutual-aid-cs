<template>
	<view>
		<!--我的评论，收藏，发布，点赞,展示信息-->
		<view class="cu-list menu-avatar">
			<view class="cu-item" :class="modalName=='move-box-'+ index?'move-cur':''" v-for="(item,index) in dataList"
				:key="index" @touchstart="ListTouchStart" @touchmove="ListTouchMove" @touchend="ListTouchEnd"
				:data-target="'move-box-' + index" v-if="type!=2">

				<view class="content">
					<view class="text-grey">{{item.confess.title}}</view>
					<view class="text-gray text-sm">
						<text class="cuIcon-time text-red  margin-right-xs"></text> {{item.confess.time}}
					</view>
				</view>
				<view class="action">
					<view class="text-grey text-xs">
						{{item.confess.type==0?'表白':item.confess.type==1?'闲置':item.confess.type==2?'寻物':吐槽}}
					</view>
				</view>
				<view class="move">
					<view class="bg-grey" @click="detail(item.confess.id)">详情</view>
					<view class="bg-red" @click="del(item.id)">删除</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				dataList: [],
				title: "",
				modalName: null,
				listTouchStart: 0,
				listTouchDirection: null,
				type: 0,
				dataConfesslist: [],
				confess: {},
				data:[]
			}
		},
		onLoad(a) {
			var that = this
			let e = a.type
			that.type = e
			that.$apiRequest({
					url: '/api/user/mine',
					method: 'GET',
				})
				.then(res => {

					if (that.type == 0) {
						that.dataList = res.data.data.mineCollect
						that.title = "我的收藏"
					}
					if (that.type == 1) {
						that.dataList = res.data.data.likes
						that.title = "我的点赞"
					}
					if (that.type == 2) {
						that.dataConfesslist = res.data.data.mineConfess
						that.title = "我的发布"
					}
					if (that.type == 3) {
						that.dataList = res.data.data.mineComment
						that.title = "我的评论"
					}
					console.log(that.title)
					uni.setNavigationBarTitle({
						title: that.title
					})
				})
		},
		methods: {
			// ListTouch触摸开始
			ListTouchStart(e) {
				this.listTouchStart = e.touches[0].pageX
			},

			// ListTouch计算方向
			ListTouchMove(e) {
				this.listTouchDirection = e.touches[0].pageX - this.listTouchStart > 0 ? 'right' : 'left'
			},
			// ListTouch计算滚动
			ListTouchEnd(e) {
				if (this.listTouchDirection == 'left') {
					this.modalName = e.currentTarget.dataset.target
				} else {
					this.modalName = null
				}
				this.listTouchDirection = null

			},
			detail(e) {
				var that = this
				that.$apiRequest({
						url: '/api/confess/getById',
						method: 'POST',
						data: {
							id: e,
						}
					})
					.then(res => {
						that.confess = res.data.data
						uni.navigateTo({
							url: '../../pages/detail/detail?id=' + JSON.stringify(that.confess)
						})
					})

			},
			del(e) {
				var that = this
				var urls = ''
				console.log(that.type)
				if (that.type == 0) {
					urls="collect"

				}
				if (that.type == 1) {
					urls="like"
					console.log(urls)
				}
				if (that.type == 2) {
					urls="confess"

				}
				if (that.type == 3) {
					urls="comment"

				}

				that.$apiRequest({
						url: '/api/' + urls + '/del',
						method: 'POST',
						data: {
							id: e,
						}
					})
					.then(res => {
						that.$apiRequest({
								url: '/api/user/mine',
								method: 'GET',
							})
							.then(res => {

								if (that.type == 0) {
									that.dataList = res.data.data.mineCollect
									that.title = "我的收藏"
								}
								if (that.type == 1) {
									that.dataList = res.data.data.likes
									that.title = "我的点赞"
								}
								if (that.type == 2) {
									that.dataList = res.data.data.mineConfess
									that.title = "我的发布"
								}
								if (that.type == 3) {
									that.dataList = res.data.data.mineComment
									that.title = "我的评论"
								}

							})
					})


			},
			remove(e,a){
				console.log(e)
				var that = this

			}
		}
	}
</script>

<style>

</style>

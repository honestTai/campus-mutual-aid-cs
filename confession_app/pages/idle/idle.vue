<template style="background-color: white;">
	<view>
		<view class="cu-bar search bg-white">
			<view class="search-form round">
				<text class="cuIcon-search"></text>
				<input @input="onInput" :adjust-position="false" type="text" placeholder="输入想要查询的信息" confirm-type="search" v-model="likeString"></input>
			</view>
			<view class="action">
				<button class="cu-btn bg-green shadow-blur round" @click="Search">搜索</button>
			</view>
		</view>
		<!--卡片列表展示-->
		<view class="cu-bar bg-white solid-bottom">
			<view class="action" >
				<uni-datetime-picker v-model="range" type="daterange" @maskClick="maskClick" />
			</view>
		</view>
		<!--列表循环展示，下拉刷新-->
		<view v-for="(item,index) in confessList" :key="index">
			<view class="cu-card case" >
				<view class="cu-item shadow">
					<view class="image" @click="detailInfo(item)">
						<image :src="item.imgList!=null?item.imgList[0]:'http://127.0.0.1:8776/api/confess/nopic.jpg'" mode="widthFix">
						</image>
						<view class="cu-tag bg-blue">闲置</view>
						<view class="cu-bar bg-shadeBottom"> <text class="text-cut">{{item.title}}</text></view>
					</view>
					<view class="cu-list menu-avatar">
						<view class="cu-item">
							<view class="cu-avatar round lg"
								:style=" 
								     {backgroundImage:'url('+item.user.image+')'}">
							</view>
							<view class="content flex-sub">
								<view class="text-grey">{{item.user.name}}</view>
								<view class="text-gray text-sm flex justify-between">
									{{item.time}}
									<view class="text-gray text-sm">
										<text class="cuIcon-favor margin-lr-xs" @click="addCollect(item.id)"></text>{{item.collectCount}}
										<text class="cuIcon-appreciate margin-lr-xs"@click="addLike(item.id)"></text> {{item.likeCount}}
										<text class="cuIcon-message margin-lr-xs"></text> {{item.commentCount}}
									</view>
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		<!--加载更多-->
		<uniLoadMore :status="status" :icon-size="14" :content-text="contentText" />
		<!--悬浮按钮-->
		<fab-com></fab-com>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
				reload: false,
				status: 'more',
				contentText: {
					contentdown: '上拉加载更多~',
					contentrefresh: '加载中',
					contentnomore: '我是有底线的~'
				},
				//表白墙内容List
				confessList: [],
				pageIndex: 1,
				pageSize: 10,
				totalCount: 0,
				user: uni.getStorageSync('user'),
				range: [],
				likeString:""
			}
		},
		onShow() {
			this.likeString=""
			this.confessList=[]
			this.getAllConfess()
		},
		watch: {
			range(newval) {
				var that=this
				that.confessList=[]
				that.$apiRequest({
						url: '/api/confess/getList',
						method: 'POST',
						data: {
							pageSize: that.pageSize,
							pageIndex: that.pageIndex,
							type: 1,
							dateList:this.range,
							likeString:this.likeString
						}
					})
					.then(res => {
						
						let data = res.data.data
						console.log(data)
						that.totalCount = data.total
						if (data.total > 0) {
							const dataMap = data.records
							console.log(dataMap)
							that.confessList = that.reload ? dataMap : that.confessList.concat(dataMap);
							that.reload = false;
						} else {
							that.confessList = [];
						}
						if (that.totalCount == that.confessList.length) {
							that.reload = false;
							that.status = 'noMore'
						}
						console.log(that.confessList)
					})
			}
		},
		methods: {
			getAllConfess() {
				console.log(1)
				var that=this
				that.$apiRequest({
						url: '/api/confess/getList',
						method: 'POST',
						data: {
							pageSize: that.pageSize,
							pageIndex: that.pageIndex,
							type: 1,
							likeString:that.likeString
						}
					})
					.then(res => {
						
						let data = res.data.data
						console.log(data)
						that.totalCount = data.total
						if (data.total > 0) {
							const dataMap = data.records
							console.log(dataMap)
							that.confessList = that.reload ? dataMap : that.confessList.concat(dataMap);
							that.reload = false;
						} else {
							that.confessList = [];
						}
						if (that.totalCount == that.confessList.length) {
							that.reload = false;
							that.status = 'noMore'
						}
						console.log(that.confessList)
					})
			},
			nReachBottom() {
				if (this.totalCount > this.confessList.length) {
					this.status = 'loading';
					setTimeout(() => {
						this.pageIndex++
						this.getAllConfess(); //执行的方法
					}, 1000) //这里我是延迟一秒在加载方法有个loading效果，如果接口请求慢的话可以去掉
				} else { //停止加载
					this.status = 'noMore'
				}
			},
			cardSwiper(e) {
				this.cardCur = e.detail.current
			},
			onClick(e) {
				console.log(e)
			},
			actionsClick(text) {
				uni.showToast({
					title: text,
					icon: 'none'
				})
			},
			addCollect(e){
				var that=this
				that.$apiRequest({
						url: '/api/collect/add',
						method: 'POST',
						data: {
							pkId: e,
						}
					})
					.then(res => {
						uni.showToast({
							icon: 'success',
							title: '收藏成功'
						});
						setTimeout(function(){
							that.confessList=[]
							that.getAllConfess()
						}, 1000)
					})
			},
			addLike(e){
				var that=this
				that.$apiRequest({
						url: '/api/like/add',
						method: 'POST',
						data: {
							pkId: e,
						}
					})
					.then(res => {
						uni.showToast({
							icon: 'success',
							title: '点赞成功'
						});
						setTimeout(function(){
							that.confessList=[]
							that.getAllConfess()
						}, 1000)
					})
			},
			maskClick(e) {
				console.log('----maskClick事件:', e);
			},
			detailInfo(e){
				uni.navigateTo({
					url:"/pages/detail/detail?id="+JSON.stringify(e)
				})
			},
			onInput(e){
			
				this.likeString=e.target.value
				
			},
			Search(){
				this.confessList=[]
				this.getAllConfess()
			}
		}
	}
</script>

<style lang="scss">
	.tower-swiper .tower-item {
		transform: scale(calc(0.5 + var(--index) / 10));
		margin-left: calc(var(--left) * 100upx - 150upx);
		z-index: var(--index);
	}

	.container {
		overflow: hidden;
	}

	.custom-cover {
		flex: 1;
		flex-direction: row;
		position: relative;
	}

	.cover-content {
		position: absolute;
		bottom: 0;
		left: 0;
		right: 0;
		height: 40px;
		background-color: rgba($color: #000000, $alpha: 0.4);
		display: flex;
		flex-direction: row;
		align-items: center;
		padding-left: 15px;
		font-size: 14px;
		color: #fff;
	}

	.card-actions {
		display: flex;
		flex-direction: row;
		justify-content: space-around;
		align-items: center;
		height: 45px;
		border-top: 1px #eee solid;
	}

	.card-actions-item {
		display: flex;
		flex-direction: row;
		align-items: center;
	}

	.card-actions-item-text {
		font-size: 12px;
		color: #666;
		margin-left: 5px;
	}

	.cover-image {
		flex: 1;
		height: 150px;
	}

	.no-border {
		border-width: 0;
	}
</style>

<template>
	<view>
		<view class="cu-list menu-avatar">
		<view class="cu-item" :class="modalName=='move-box-'+ item.id?'move-cur':''" v-for="(item,index) in dataConfesslist"
			:key="item.id" @touchstart="ListTouchStart" @touchmove="ListTouchMove" @touchend="ListTouchEnd"
			:data-target="'move-box-' + item.id" >
		
			<view class="content">
				<view class="text-grey">{{item.title}}</view>
				<view class="text-gray text-sm">
					<text class="cuIcon-infofill text-red  margin-right-xs"></text> {{item.time}}
				</view>
			</view>
			<view class="action">
				<view class="text-grey text-xs">{{item.type==0?'表白':item.type==1?'闲置':item.type==2?'寻物':吐槽}}</view>
			</view>
			<view class="move">
				<view class="bg-grey" @click="detail(item.id)">详情</view>
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
	
						that.dataConfesslist = res.data.data.mineConfess
						that.title = "我的发布"
					
					
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
				
				that.$apiRequest({
						url: '/api/' + 'confess' + '/del',
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
				
						
									that.dataConfesslist = res.data.data.mineConfess
									that.title = "我的发布"
								
				
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

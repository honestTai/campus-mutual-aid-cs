<template>
	<view>
		<!--主题内容展示-->
		<view class="cu-card dynamic" :class="isCard?'no-card':''">
			<view class="cu-item shadow">
				<view class="cu-list menu-avatar">
					<view class="cu-item">
						<view class="cu-avatar round lg" :style=" {backgroundImage:'url('+confess.user.image+')'}">
						</view>
						<view class="content flex-sub">
							<view>{{confess.user.name}}</view>
							<view class="text-gray text-sm flex justify-between">
								{{confess.time}}
							</view>
						</view>
					</view>
				</view>
				<view class="text-content">
					主要内容：{{confess.map.info}}
				</view>
        <view class="text-content">
          联系方式：{{confess.elseConnect}}
        </view>
        <view class="text-content" v-if="confess.type==1">
          <view class="text-content">
            价格：{{confess.map.price}}
          </view>
          <view class="text-content">
            信息备注：{{confess.map.thing}}
          </view>
        </view>
        <view class="text-content"  v-if="confess.type==2">
          <view class="text-content">
            物品名称/描述：{{confess.map.thing}}
          </view>
          <view class="text-content">
            信息备注：{{confess.map.remark}}
          </view>
          <view class="text-content">
            丢失/拾取地点：{{confess.map.address}}
          </view>

        </view>
        <view class="text-content"  v-if="confess.type==3">
          {{confess.map.info}}
        </view>

				<!--轮播-->
				<swiper class="screen-swiper" :class="dotStyle?'square-dot':'round-dot'" :indicator-dots="true"
					:circular="true" :autoplay="true" interval="5000" duration="500">
					<swiper-item v-for="(item,index) in confess.imgList" :key="index">
						<image :src="item" mode="aspectFill"></image>
					</swiper-item>
				</swiper>

				<view class="text-gray text-sm text-right padding">
					<text class="cuIcon-favor margin-lr-xs"
						@click="addCollect(confess.id)"></text>{{confess.collectCount}}
					<text class="cuIcon-appreciate margin-lr-xs" @click="addLike(confess.id)"></text>
					{{confess.likeCount}}
					<text class="cuIcon-message margin-lr-xs"></text> {{confess.commentCount}}
				</view>
				<!--评论-->
				<view class="cu-list menu-avatar comment solids-top" v-for="(comment,index) in commentList"
					:key="index">
					<view class="cu-item">
						<view class="cu-avatar round" :style=" {backgroundImage:'url('+comment.user.image+')'}">
						</view>
						<view class="content">
							<view class="text-grey">{{comment.user.name}}</view>
							<view class="text-gray text-content text-df">
								{{comment.info}}
							</view>
							<view class="bg-grey padding-sm radius margin-top-sm  text-sm" v-if="comment.reply!=null">
								<view class="flex">
									<view>{{confess.user.name}}：</view>
									<view class="flex-sub">{{comment.reply}}</view>

								</view>
								<view class="flex">
									<view class="text-white text-df">{{comment.replyTime}}</view>
								</view>
							</view>
							<view class="margin-top-sm flex justify-between">
								<view class="text-gray text-df">{{comment.time}}</view>
								<view>
									<text class="cuIcon-messagefill text-gray margin-left-sm"
										v-if="comment.reply==null && user.user.id==confess.userId"
										@click="replyComment(comment.id)">回复</text>
									<text class="cuIcon-deletefill text-gray margin-left-sm"
										v-if="comment.reply==null && user.user.id==confess.userId"
										@click="delComment(comment.id)">删除</text>
								</view>
							</view>
						</view>
					</view>

				</view>
				<view class="padding-xl bg-white solid-bottom" style="border-radius: 5px;margin: 16px;">
					<form @submit="addComment">
						<view class="cu-form-group">
							<input placeholder="请输入您的评论内容" name="info"></input>
						</view>
						<button form-type="submit" class="cu-btn block line-blue  lg">评论</button>
					</form>
				</view>
			</view>
		</view>

		<view class="cu-modal" :class="modalName=='DialogModal1'?'show':''">
			<view class="cu-dialog">
				<view class="cu-bar bg-white justify-end">
					<view class="content">评论回复</view>
					<view class="action" @tap="hideModal">
						<text class="cuIcon-close text-red"></text>
					</view>
				</view>
				<view class="padding-xl">
					<input placeholder="请输入回复内容" name="info" v-model="reply" @input="onInput"></input>
				</view>
				<view class="cu-bar bg-white justify-end">
					<view class="action">
						<button class="cu-btn line-green text-green" @tap="hideModal">取消</button>
						<button class="cu-btn bg-green margin-left" @tap="replyConfirm">确定</button>

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
				confess: {},
				isCard: false,
				dotStyle: false,
				commentList: [],
				user: uni.getStorageSync('user'),
				modalName: null,
				reply: "",
				commentId: null
			}
		},
		onLoad(e) {
			console.log(JSON.parse(e.id))
			this.confess = JSON.parse(e.id)
			uni.setNavigationBarTitle({
				title: this.confess.title
			})
			this.getAllComment()
		},
		methods: {
			getAllComment() {
				var that = this
				that.$apiRequest({
						url: '/api/comment/getAll',
						method: 'POST',
						data: {
							pkId: that.confess.id,
						}
					})
					.then(res => {
						that.commentList = res.data.data
					})
			},
			//刷新
			refresh(e) {
				var that = this
				that.$apiRequest({
						url: '/api/confess/getById',
						method: 'POST',
						data: {
							id: this.confess.id,
						}
					})
					.then(res => {
						this.confess = res.data.data
					})
			},
			addCollect(e) {
				var that = this
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
						setTimeout(function() {

							that.refresh()
						}, 1000)
					})
			},
			addLike(e) {
				var that = this
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
						setTimeout(function() {

							that.refresh()
						}, 1000)
					})
			},
			addComment(e) {
				var that = this
				if (e.detail.value.info.length < 0) {
					uni.showToast({
						icon: 'none',
						title: '请输入评论内容'
					});
					return;
				}
				that.$apiRequest({
						url: '/api/comment/add',
						method: 'POST',
						data: {
							info: e.detail.value.info,
							pkId: this.confess.id
						}
					})
					.then(res => {
						uni.showToast({
							icon: 'success',
							title: '评论成功'
						});
						setTimeout(function() {
							that.getAllComment()
							that.refresh()
						}, 1000)
					})
			},
			replyComment(e) {
				this.commentId = e
				this.modalName = 'DialogModal1'
			},
			hideModal(e) {
				this.modalName = null
			},
			onInput(e) {
				this.reply = e.target.value
			},
			replyConfirm(e) {
				var that = this
				that.$apiRequest({
						url: '/api/comment/add',
						method: 'POST',
						data: {
							reply: that.reply,
							id: that.commentId
						}
					})
					.then(res => {
						uni.showToast({
							icon: 'success',
							title: '回复成功'
						});
						setTimeout(function() {
							that.reply = ""
							that.modalName = null
							that.getAllComment()
							that.refresh()

						}, 1000)
					})
			},
			delComment(e) {
				var that = this
				that.$apiRequest({
						url: '/api/' + 'comment' + '/del',
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
								that.getAllComment()
								that.refresh()

							})
					})
			}
		}
	}
</script>

<style>

</style>

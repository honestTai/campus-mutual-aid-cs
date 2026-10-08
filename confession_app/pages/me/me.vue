<template>
  <view>
    <view class="person-head">
      <cmd-avatar :src="userInfo.image" @click="fnInfoWin" size="lg" :make="{'background-color': '#fff'}"></cmd-avatar>
      <view class="person-head-box">
        <view class="person-head-nickname">{{userInfo.name}}</view>
      </view>
    </view>
    <view class="person-list">
      <cmd-cell-item title="我的收藏" slot-left arrow @click="mineData(0)">
        <cmd-icon type="bullet-list" size="24" color="#368dff"></cmd-icon>
      </cmd-cell-item>
      <cmd-cell-item title="我的点赞" slot-left arrow @click="mineData(1)">
        <cmd-icon type="message" size="24" color="#368dff"></cmd-icon>
      </cmd-cell-item>
      <cmd-cell-item title="我的发布" slot-left arrow @click="mineData(2)">
        <cmd-icon type="settings" size="24" color="#368dff"></cmd-icon>
      </cmd-cell-item>
      <cmd-cell-item title="评论"  slot-left arrow @click="mineData(3)">
        <cmd-icon type="alert-circle" size="24" color="#368dff"></cmd-icon>
      </cmd-cell-item>
    </view>
	<view class="padding flex flex-direction">
		<button form-type="submit" class="cu-btn bg-cyan lg" @click="removeLogin()">退出登录</button>
	</view>
	<!--悬浮按钮-->
	<fab-com></fab-com>
  </view>
  
</template>

<script>
  import cmdAvatar from "@/components/cmd-avatar/cmd-avatar.vue"
  import cmdIcon from "@/components/cmd-icon/cmd-icon.vue"
  import cmdCellItem from "@/components/cmd-cell-item/cmd-cell-item.vue"

  export default {
    components: {
      cmdAvatar,
      cmdCellItem,
      cmdIcon
    },
    data() {
      return {
		  userInfo:{},
		  mines:{}
	  };
    },
	onShow() {
		this.getMine()
	},
    methods: {
		getMine(){
			var that=this
			that.$apiRequest({
					url: '/api/user/mineInfo',
					method: 'GET',
				})
				.then(res => {
					this.userInfo=res.data.data
				})
		},
		mineData(e){
			if(e==2){
				uni.navigateTo({
				  url: '../mineConfess/mineConfess'
				})
			}else{
				uni.navigateTo({
				  url: '../mineData/mineData?type='+e
				})
			}
			
		},
      /**
       * 打开用户信息页
       */
      fnInfoWin() {
        uni.navigateTo({
          url: '../userInfo/userInfo?userInfo='+JSON.stringify(this.userInfo)
        })
      },
	  removeLogin(){
		  uni.removeStorageSync("user")
		  uni.switchTab({
		  	url: '../confess/confess'
		  });
	  }
    }
  }
</script>

<style>
  .person-head {
    display: flex;
    flex-direction: row;
    align-items: center;
    height: 150px;
    padding-left: 20px;
    background: linear-gradient(to right, #365fff, #36bbff);
  }

  .person-head-box {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    margin-left: 10px;
  }

  .person-head-nickname {
    font-size: 18px;
    font-weight: 500;
    color: #fff;
  }

  .person-head-username {
    font-size: 14px;
    font-weight: 500;
    color: #fff;
  }

  .person-list {
    line-height: 0;
  }
</style>

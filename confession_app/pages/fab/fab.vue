<template>
	<view>
		<uni-fab ref="fab" :pattern="pattern" :content="content" :horizontal="horizontal" :vertical="vertical"
			:direction="direction" @trigger="trigger"  />
	</view>
</template>

<script>
export default {
	components: {},
	data() {
		return {
			direction: 'horizontal',
			horizontal: 'left',
			vertical: 'bottom',
			pattern: {
				color: '#7A7E83',
				backgroundColor: '#fff',
				selectedColor: '#007AFF',
				buttonColor: '#fff',
				iconColor: '#aaa'
			},
			content: [
				{
					iconPath: '/static/love.png',
					selectedIconPath: '/static/love.png',
					text: '表白墙',
					active: false
				},
				{
					iconPath: '/static/idles.png',
					selectedIconPath: '/static/idles.png',
					text: '闲置出售',
					active: false
				},
				{
					iconPath: '/static/finds.png',
					selectedIconPath: '/static/finds.png',
					text: '物品寻找',
					active: false
				}
			]
		};
	},
	methods: {
		trigger(e) {
			console.log(e)
			let type=e.index
			//跳转页面
			uni.navigateTo({
				url: '../form/form?type='+parseInt(type)
			});
		},
	}
};
</script>

<style>
	.warp {
		padding: 10px;
	}

	.button {
		margin-bottom: 10px;
	}
</style>

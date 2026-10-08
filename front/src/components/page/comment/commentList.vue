<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 评论列表
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">

                <el-input v-model="query.likeString" placeholder="输入查询内容" class="handle-input mr10"
                          @change="handleSearchRealName"></el-input>
            </div>

            <el-table
                :data="commentLists"
                row-key="id"
                border
                default-expand-all
            >
                <el-table-column prop="user.name" label="发布用户" width="160" align="center"></el-table-column>
                <el-table-column prop="time" label="时间" align="center" width="160"></el-table-column>
                <el-table-column prop="info" label="内容" align="center"></el-table-column>
                <el-table-column prop="confess.title" label="被评论内容标题" align="center"></el-table-column>
                <el-table-column prop="reply" label="回复内容" align="center"></el-table-column>
                <el-table-column label="操作" width="180" align="center" fixed="right">
                    <template slot-scope="scope">
                        <el-button
                            class="green"
                            @click="deleteCommentsOrPhotoCircle(scope.row.id)"
                        >删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                    background
                    layout="total,sizes, prev, pager, next, jumper"
                    @size-change="handlePageSizeChange"
                    @current-change="handlePageChange"
                    :current-page="query.pageIndex"
                    :page-sizes="[10, 50, 100, 200,500,1000]"
                    :page-size="query.pageSize"
                    :total="pageTotal"
                >
                </el-pagination>
            </div>
        </div>
    </div>

</template>

<script>
import { commentList, del} from '../../../utils';

export default {
    name: 'basetable',
    data() {
        return {
            query: {
                pageIndex: 1,
                pageSize: 10,
                likeString:'',
                delType:0,
                id:null
            },
            //上次登录时间与地址
            commentLists: [],
            pageTotal: 0,
            dialogVisible: false,
            opinion:{
                reply:"",
                id:""
            },
            id:""
        };
    },
    created() {
        this.getCommentList();
    },
    methods: {

        getCommentList() {
            commentList(this.query).then(res => {
                let data = res.data;
                this.pageTotal = data.data.total;
                this.commentLists = data.data.records;
            });
        },
        //分页跳转
        handlePageChange(val) {
            this.$set(this.query, 'pageIndex', val);
            this.getCommentList();
        },
        deleteCommentsOrPhotoCircle(e,a){
            this.query.id=e
            del(this.query).then(res=>{
                this.$message.success(res.data.message);
                this.getCommentList();
            })
        },
        //每页大小改变
        handlePageSizeChange(val) {
            this.$set(this.query, 'pageSize', val);
            this.getCommentList();
        },
        //发表用户姓名
        handleSearchRealName(val) {
            this.$set(this.query, 'likeString', val);
            this.getCommentList();
        }
    }
};
</script>

<style scoped>
.handle-box {
    margin-bottom: 20px;
}

.handle-select {
    width: 120px;
}

.handle-input {
    width: 300px;
    display: inline-block;
}

.table {
    width: 100%;
    font-size: 14px;
}

.red {
    color: #ff0000;
}

.mr10 {
    margin-right: 10px;
}

</style>

<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 内容管理
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">

            <div class="handle-box">

                <el-input v-model="query.likeString" placeholder="请输入查询内容" class="handle-input mr10"
                          @change="handleSearchRealName"></el-input>
            </div>
            <el-table
                :data="confessList"
                border
                class="table"
                ref="multipleTable"
                header-cell-class-name="table-header"
            >

                <el-table-column prop="id" label="序号" width="160" align="center"></el-table-column>
                <el-table-column prop="title" label="标题" align="center" width="160"></el-table-column>
                <el-table-column label="图片" align="center">
                    <template slot-scope="scope">
                        <el-form label-position="left" inline class="demo-table-expand">
                            <el-form-item>
                                <div class="demo-image__preview">
                                    <el-image
                                        style="width: 100px; height: 100px"
                                        :src="scope.row.imgList[0]"
                                        v-if="scope.row.imgList!=null "
                                    >
                                    </el-image>
                                </div>
                            </el-form-item>
                        </el-form>
                    </template>
                </el-table-column>
                <el-table-column prop="user.name" label="发布人" align="center" width="160"></el-table-column>
                <el-table-column prop="time" label="发布时间" align="center" width="160"></el-table-column>
                <el-table-column prop="time" label="类型" align="center" width="160">
                    <template slot-scope="scope">
                        <el-form label-position="left" inline class="demo-table-expand">
                            <el-form-item>
                                <div class="demo-image__preview">
                                   <span>
                                       {{scope.row.type==0?'表白':scope.row.type==1?'闲置':scope.row.type==2?'寻物':'吐槽'}}
                                   </span>
                                </div>
                            </el-form-item>
                        </el-form>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="180" align="center" fixed="right">
                    <template slot-scope="scope">
                        <el-button
                            class="green"
                            @click="delData(scope.row.id)"
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
import { list, del } from '../../../utils';

export default {
    props: ['id'],
    name: 'basetable',
    data() {
        return {
            query: {
                pageIndex: 1,
                pageSize: 10,
                likeString: '',
                idList: [],
                type: null,
                id: null,
                delType:1,
                userId:null
            },
            //上次登录时间与地址
            confessList: [],
            pageTotal: 0
        };
    },
    watch: {
        id(newId, oldId) {
            console.log('New id:', newId);
            // 在这里可以调用你的方法或执行其他操作
            this.query.userId = newId
            this.getList();
        }
    },
    created() {
        console.log(this.id)
        this.getList();
    },
    methods: {
        //获取用户列表
        getList() {
            list(this.query).then(res => {
                let data = res.data;
                console.log(data)
                this.pageTotal = data.data.total;
                this.confessList = data.data.records;
            });
        },
        //删除操作
        delData(e) {
            this.query.id=e
            del(this.query).then(res => {
                this.$message.success(res.data.message);
                list(this.query).then(res => {
                    let data = res.data;
                    console.log(data)
                    this.pageTotal = data.data.total;
                    this.confessList = data.data.records;
                });
            });
        },
        //分页跳转
        handlePageChange(val) {
            this.$set(this.query, 'pageIndex', val);
            this.getList();
        },
        //每页大小改变
        handlePageSizeChange(val) {
            this.$set(this.query, 'pageSize', val);
            this.getList();
        },

        handleSearchRealName(val) {
            this.$set(this.query, 'title', val);
            this.getList();
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

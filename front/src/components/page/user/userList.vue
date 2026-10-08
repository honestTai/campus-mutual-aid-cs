<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 前台用户管理
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">

                <el-input v-model="query.likeString" placeholder="输入查询内容" class="handle-input mr10"
                          @change="handleSearchRealName"></el-input>
            </div>

            <el-table
                :data="appUserList"
                border
                class="table"
                ref="multipleTable"
                header-cell-class-name="table-header"
            >

                <el-table-column prop="id" label="序号" width="160" align="center"></el-table-column>
                <el-table-column prop="id" label="头像" width="160" align="center">
                    <template slot-scope="scope">
                        <el-form label-position="left" inline class="demo-table-expand">
                            <el-form-item>
                                <div class="demo-image__preview">
                                    <el-image
                                        style="width: 100px; height: 100px"
                                        :src="scope.row.image"
                                    >
                                    </el-image>
                                </div>
                            </el-form-item>
                        </el-form>
                    </template>
                </el-table-column>
                <el-table-column prop="name" label="名称" align="center" width="160"></el-table-column>
                <el-table-column prop="number" label="登录账号" align="center"></el-table-column>
                <el-table-column label="操作"  align="center" fixed="right">

                    <template slot-scope="scope">
                        <el-button
                            class="red"
                            @click="fenjin(scope.row.id,0)"
                            v-if="scope.row.status === 1"
                        >封禁
                        </el-button>
                        <el-button
                            class="red"
                            @click="fenjin(scope.row.id,1)"
                            v-if="scope.row.status === 0"
                        >解封
                        </el-button>
                        <el-button
                            class="red"
                            @click="deleteAdminUser(scope.row.id)"
                        >查看发布的内容
                        </el-button>
<!--                        <el-button-->
<!--                            class="red"-->
<!--                            @click="deleteAdminUser(scope.row.id)"-->
<!--                        >删除-->
<!--                        </el-button>-->
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
        <el-dialog title="发布内容查看" :visible.sync="homeInfo" width="80%" :close-on-click-modal="false">
            <home :id="query.id"></home>
        </el-dialog>
    </div>
</template>

<script>
import { userList, del, fenjin } from '../../../utils';
import home from '../../page/home/Home.vue';
export default {
    components: {
        home
    },
    name: 'basetable',
    data() {
        return {
            query: {
                pageIndex: 1,
                pageSize: 10,
                id:null,
                delType:3,
                likeString:""
            },
            appUserList: [],
            pageTotal: 0,
            homeInfo:false
        };
    },
    created() {
        this.getappUserList();
    },
    methods: {
        handleSearchRealName(val) {
            this.$set(this.query, 'likeString', val);
            this.getappUserList();
        },
        getappUserList() {
            userList(this.query).then(res => {
                let data = res.data;
                this.pageTotal = data.data.total;
                this.appUserList = data.data.records;
            });
        },

        //删除操作
        deleteAdminUser(e) {
            this.homeInfo = true
            this.query.id=e
            // del(this.query).then(res => {
            //     this.$message.success(res.data.message);
            //     this.getappUserList();
            // });

        },
        fenjin(e,a){
            let data = {
                id:e,
                status:a
            }
            fenjin(data).then(res => {
                this.$message.success(res.data.message);
                this.getappUserList();
            });

        },
        //分页跳转
        handlePageChange(val) {
            this.$set(this.query, 'pageIndex', val);
            this.getappUserList();
        },
        //每页大小改变
        handlePageSizeChange(val) {
            this.$set(this.query, 'pageSize', val);
            this.getappUserList();
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

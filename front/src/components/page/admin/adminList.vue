<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 后台用户管理
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-button
                    type="primary"
                    icon="el-icon-lx-add"
                    class="handle-del mr10"
                    @click="addUser"
                >新增用户
                </el-button>
            </div>


            <el-table
                :data="adminUserList"
                border
                class="table"
                ref="multipleTable"
                header-cell-class-name="table-header"
            >

                <el-table-column prop="id" label="序号" width="160" align="center"></el-table-column>
                <el-table-column prop="number" label="账号" align="center" width="160"></el-table-column>
                <el-table-column prop="password" label="密码" align="center"></el-table-column>
                <el-table-column label="操作" width="180" align="center" fixed="right">

                    <template slot-scope="scope">
                        <el-button
                            class="green"
                            @click="updateUser(scope.row)"
                        >编辑
                        </el-button>
                        <el-button
                            class="red"
                            @click="deleteAdminUser(scope.row.id)"
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
import { adminList, del, addAdmin } from '../../../utils';

export default {
    name: 'basetable',
    data() {
        return {
            query: {
                pageIndex: 1,
                pageSize: 10,
                id:null,
                delType:2
            },
            adminUserList: [],
            pageTotal: 0
        };
    },
    created() {
        this.getadminUserList();
    },
    methods: {
        updateUser(e){
            localStorage.setItem('admin', JSON.stringify(e));
            this.$router.push('/addUser');
        },
        getadminUserList() {
            adminList(this.query).then(res => {
                let data = res.data;
                this.pageTotal = data.data.total;
                this.adminUserList = data.data.records;
            });
        },

        //删除操作
        deleteAdminUser(e) {
            this.query.id=e
            del(this.query).then(res => {
                this.$message.success(res.data.message);
                this.getadminUserList();
            });

        },
        //分页跳转
        handlePageChange(val) {
            this.$set(this.query, 'pageIndex', val);
            this.getadminUserList();
        },
        //每页大小改变
        handlePageSizeChange(val) {
            this.$set(this.query, 'pageSize', val);
            this.getadminUserList();
        },
        addUser() {
            localStorage.setItem('admin', "");
            this.$router.push('/addUser');
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

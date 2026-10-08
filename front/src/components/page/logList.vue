<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 日志查看
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
                <el-table-column prop="info" label="操作内容" align="center" width="160"></el-table-column>
                <el-table-column prop="methods" label="操作方法" align="center" width="160"></el-table-column>
                <el-table-column prop="classname" label="类名" align="center" width="160"></el-table-column>
                <el-table-column prop="user" label="操作用户" align="center" width="160"></el-table-column>
                <el-table-column prop="params" label="参数" align="center" ></el-table-column>
                <el-table-column prop="status" label="状态" align="center" width="160"></el-table-column>
                <el-table-column prop="time" label="操作时间" align="center" width="160"></el-table-column>
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
import { list, logLst } from '../../utils';

export default {
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
                delType:1
            },
            //上次登录时间与地址
            confessList: [],
            pageTotal: 0
        };
    },
    created() {
        this.getList();
    },
    methods: {
        //获取用户列表
        getList() {
            logLst(this.query).then(res => {
                let data = res.data;
                console.log(data)
                this.pageTotal = data.data.total;
                this.confessList = data.data.records;
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

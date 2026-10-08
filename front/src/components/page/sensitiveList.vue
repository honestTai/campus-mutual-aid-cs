<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 敏感词管理
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">

            <div class="handle-box">

                <el-input v-model="query.likeString" placeholder="请输入查询内容" class="handle-input mr10"
                          @change="handleSearchRealName"></el-input>

                <el-button
                    class="green"
                    @click="update(-1)"
                >增加
                </el-button>
            </div>
            <el-table
                :data="confessList"
                border
                class="table"
                ref="multipleTable"
                header-cell-class-name="table-header"
            >
                <el-table-column prop="id" label="序号" width="160" align="center"></el-table-column>
                <el-table-column prop="info" label="内容" align="center" width="160"></el-table-column>
                <el-table-column prop="status" label="状态" align="center" width="160">
                    <template slot-scope="scope">
                        {{ scope.row.statu === '0'|| scope.row.statu === 0 ? '使用' : '不使用' }}
                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center" fixed="right">
                    <template slot-scope="scope">
                        <el-button
                            class="green"
                            @click="update(scope.row)"
                        >修改
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
        <el-dialog title="敏感词编辑" :visible.sync="sensitiveSaveAndUpdate" width="80%" :close-on-click-modal="false">
            <div class="block">
                <el-form ref="form" :model="sensitive" label-width="80px">
                    <el-form-item label="内容">
                        <el-input v-model="sensitive.info"></el-input>
                    </el-form-item>
                    <el-form-item label="是否启用">
                        <el-select v-model="sensitive.statu" placeholder="是否启用">
                            <el-option label="使用" value='0'></el-option>
                            <el-option label="不使用" value='1'></el-option>
                        </el-select>
<!--                        <el-switch v-model="sensitive.statu" :value="sensitive.statu===0"></el-switch>-->
                    </el-form-item>
                </el-form>

            </div>
            <el-button @click="sensitiveSaveAndUpdate = false">关闭</el-button>
            <el-button @click="addSensitive()">提交</el-button>
        </el-dialog>
    </div>
</template>

<script>
import { addSensitiveApi, list, sensitive } from '../../utils';

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
                delType: 1
            },
            //上次登录时间与地址
            confessList: [],
            pageTotal: 0,
            sensitiveSaveAndUpdate: false,
            sensitive: { info: '', statu: '0', id: null }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        addSensitive() {
            addSensitiveApi(this.sensitive).then(res => {
                this.sensitiveSaveAndUpdate = false
                this.getList()
            });
        },
        //获取用户列表
        getList() {
            sensitive(this.query).then(res => {
                let data = res.data;
                console.log(data);
                this.pageTotal = data.data.total;
                this.confessList = data.data.records;
            });
        },
        update(e) {
            if(e !== -1){
                console.log(e)
                e.statu = e.statu.toString();
                this.sensitiveSaveAndUpdate = true;
                this.sensitive = { ...this.sensitive, ...e };
                console.log(this.sensitive.statu)
                // this.sensitive.statu === '0';
                console.log(this.sensitive)
            }else {
                this.sensitiveSaveAndUpdate = true;
            }

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

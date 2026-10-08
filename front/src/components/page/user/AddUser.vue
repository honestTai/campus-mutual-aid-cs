<template>
    <div>

        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-calendar"></i> 后台用户管理</el-breadcrumb-item>
                <el-breadcrumb-item>添加用户/修改用户</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <el-form ref="form" :model="form" label-width="80px">

                <el-form-item label="登录账号">
                    <el-input v-model="form.number"></el-input>
                </el-form-item>
                <el-form-item label="密码">
                    <el-input v-model="form.password" type="password"></el-input>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="save">提交</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>
<script>
import { addAdmin } from '../../../utils';

export default {
    data() {
        return {
            form: {
                number: '',
                password: '',
                id:null
            }
        };
    },
    created() {
        console.log(localStorage.getItem("admin")=='')
        if(!localStorage.getItem("admin")==''){
            this.form=JSON.parse(localStorage.getItem("admin"))
        }
    },
    methods: {
        save() {
            addAdmin(this.form).then(res => {
                this.$message.success(res.data.message);
                this.$router.push('/adminList');
            });

        }
    }
};
</script>

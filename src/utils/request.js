import axios from 'axios'
import router from "@/router";

const request = axios.create({
    baseURL: 'http://localhost:9090',
    timeout: 5000
})

// request interceptor
// can preprocess the request before sending it
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    let user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : null
    if (user) {
        config.headers['token'] = user.token;  // set the request header
    }

    return config
}, error => {
    return Promise.reject(error)
});

// response interceptor
// can process the response after receiving it
request.interceptors.response.use(
    response => {
        let res = response.data;
        // if it's a file response
        if (response.config.responseType === 'blob') {
            return res
        }
        // compatibility for string data returned by the server
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }

        if (res.code === '401') {
            // ElementUI.Message({
            //     message: res.msg,
            //     type: 'error'
            // });
            router.push("/login")
        }
        return res;
    },
    error => {
        console.log('err' + error) // debug
        return Promise.reject(error)
    }
)


export default request


const {createApp} = Vue

let app = createApp({
    data(){
        return {
            data: []
            
        }
    },
   
    methods: {
        alert(){
            Swal.fire({
                title: "This page is currently under development.",
                showClass: {
                    popup: `
                    animate__animated
                    animate__fadeInUp
                    animate__faster
                    `
                },
                hideClass: {
                    popup: `
                    animate__animated
                    animate__fadeOutDown
                    animate__faster
                    `
                }
            });
        }
       
    }
}).mount('#app')

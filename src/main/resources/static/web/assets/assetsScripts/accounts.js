const {createApp} = Vue

let app = createApp({
    data(){
        return {
            data: [],
            client: [],
            firstName : "",
            lastName : "",
            email : "",
            accounts : [],
            loans : [],
            accountId : null,
            error : "",
            selectedAccount: '',
            isModalVisible: false,
            currentPopupModal: null,
            accountType: ''
        }
    },
    created(){
        this.loadData()
        
    },

    methods: {
        loadData(){
            axios('/api/clients/current')
            .then(response =>{ 
                this.data = response,
                this.client = response.data,
                this.accounts = response.data.accounts.sort((a,b) => a.id - b.id),
                this.loans = response.data.loans,
                console.log(this.loans)
            })
            .catch(error => {
                this.error = error.response.data
                console.log(error)
            })
        },
        logOut(){
            axios('/api/logout')
            .then(
                window.location.href = "../index.html"
            )
        },
        createAccount(){
            axios.post(`/api/accounts/clients/current?type=${this.accountType}`)
            .then(response => {
                Swal.fire({
                    title: "Success",
                    text: "Account created successfully",
                    icon: "success"
                });

                setTimeout(() => {
                    window.location.href = "../pages/accounts.html";
                }, 2000);
            }).catch(error => {
                this.error = "";
        
                if (!this.accountType) {
                    this.error = "Please select an account type.";
                    return;
                }

                this.error = error.response.data
            })
        },
        openAccountSelectionModal() {
            this.showAccountSelectionModal = true;
          },
        onToggle() {
            this.isModalVisible = true;
        },
        deleteAccount(){
            axios.delete(`/api/accounts/${this.selectedAccount}`)
            .then(response => {
                Swal.fire({
                    title: "Success",
                    text: "Transaction Completed",
                    icon: "success"
                });

                setTimeout(() => {
                    window.location.href = "../pages/accounts.html";
                }, 2000);
            }).catch(error => {
                this.error = "";
        
                if (!this.selectedAccount) {
                    this.error = "Please select an account to delete.";
                    return;
                }

                this.error = error.response.data
            })
        },
        confirmDeleteAccount() {
            Swal.fire({
                title: "Confirm Deletion",
                html: `
                    <p>Are you sure you want to delete an account? This action cannot be undone.</p>
                    <label for="accountSelect">Select Account:</label>
                    <select id="accountSelect" class="swal2-input" v-model="selectedAccount">
                        <option v-for="account in accounts" :value="account.id">{{ account.number }}</option>
                    </select>
                `,
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#d33",
                cancelButtonColor: "#3085d6",
                confirmButtonText: "Delete Account"
            }).then((result) => {
                if (result.isConfirmed) {
                    this.deleteAccount();
        
                    Swal.fire({
                        title: "Success",
                        text: "Account deleted successfully",
                        icon: "success"
                    });
        
                    setTimeout(() => {
                        window.location.reload();
                    }, 2000);
                }
            });
        },

    }
}).mount('#app')


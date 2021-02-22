$(document).ready(() => {

    const categories = getCategories();
    updateCategoriesDisplay(categories);
    registerCategoryProductPurchase(categories, "");

});

const getCategories = () => {
    const sharedConfigurations = {};

        sharedConfigurations["1"] = {
            cost: { currency: "$", value: 5 },
            spec: {
                ram: { unit: "GB", value: 1 },
                cores: { unit: "vCPU", value: 1 },
                storage: { unit: "GB", value: 32, type: "SSD" },
                bandwidth: { unit: "TB", value: 1 }
            }
        };
        sharedConfigurations["2"] = {
            cost: { currency: "$", value: 10 },
            spec: {
                ram: { unit: "GB", value: 2 },
                cores: { unit: "vCPU", value: 1 },
                storage: { unit: "GB", value: 64, type: "SSD" },
                bandwidth: { unit: "TB", value: 1 }
            }
        };
        sharedConfigurations["3"] = {
            cost: { currency: "$", value: 20 },
            spec: {
                ram: { unit: "GB", value: 4 },
                cores: { unit: "vCPU", value: 2 },
                storage: { unit: "GB", value: 128, type: "SSD" },
                bandwidth: { unit: "TB", value: 2 }
            }
        };
        sharedConfigurations["4"] = {
            cost: { currency: "$", value: 25 },
            spec: {
                ram: { unit: "GB", value: 4 },
                cores: { unit: "vCPU", value: 2 },
                storage: { unit: "GB", value: 256, type: "SSD" },
                bandwidth: { unit: "TB", value: 5 }
            }
        };
        sharedConfigurations["5"] = {
            cost: { currency: "$", value: 35 },
            spec: {
                ram: { unit: "GB", value: 8 },
                cores: { unit: "vCPU", value: 4 },
                storage: { unit: "GB", value: 512, type: "SSD" },
                bandwidth: { unit: "TB", value: 5 }
            }
        };
    
        const vpsConfigurations = {};
    
        vpsConfigurations["1"] = {
            cost: { currency: "$", value: 10 },
            spec: {
                ram: { unit: "GB", value: 1 },
                cores: { unit: "vCPU", value: 1 },
                storage: { unit: "GB", value: 128, type: "M.2" },
                bandwidth: { unit: "TB", value: 1 }
            }
        };
        vpsConfigurations["2"] = {
            cost: { currency: "$", value: 15 },
            spec: {
                ram: { unit: "GB", value: 2 },
                cores: { unit: "vCPU", value: 2 },
                storage: { unit: "GB", value: 256, type: "M.2" },
                bandwidth: { unit: "TB", value: 2 }
            }
        };
        vpsConfigurations["3"] = {
            cost: { currency: "$", value: 20 },
            spec: {
                ram: { unit: "GB", value: 4 },
                cores: { unit: "vCPU", value: 2 },
                storage: { unit: "GB", value: 512, type: "M.2" },
                bandwidth: { unit: "TB", value: 5 }
            }
        };
    
        const dedicatedConfigurations = {};
    
        dedicatedConfigurations["1"] = {
            cost: { currency: "$", value: 60 },
            spec: {
                ram: { unit: "GB", value: 16 },
                cores: { unit: "CPU", value: 6 },
                storage: { unit: "TB", value: 2, type: "NVMe" },
                bandwidth: { unit: "TB", value: 25 }
            }
        };
        dedicatedConfigurations["2"] = {
            cost: { currency: "$", value: 140 },
            spec: {
                ram: { unit: "GB", value: 32 },
                cores: { unit: "CPU", value: 12 },
                storage: { unit: "TB", value: 5, type: "NVMe" },
                bandwidth: { unit: "TB", value: 50 }
            }
        };
        dedicatedConfigurations["3"] = {
            cost: { currency: "$", value: 300 },
            spec: {
                ram: { unit: "GB", value: 64 },
                cores: { unit: "CPU", value: 16 },
                storage: { unit: "TB", value: 8, type: "NVMe" },
                bandwidth: { unit: "TB", value: 100 }
            }
        };
    
        const categories = {};
    
        categories["shared"] = {
            info: "A burstable shared portion of core, suitable for simple tasks.",
            config: sharedConfigurations
        };
        categories["vps"] = {
            info: "Fully owned core virtual private server, for normal tasks.",
            config: vpsConfigurations
        };
        categories["dedicated"] = {
            info: "Fully owned core dedicated server, for high CPU computation needs.",
            config: dedicatedConfigurations
        };
    
        return categories;
}

const updateCategoriesDisplay = (categories) => {

    for(let id in categories) {
        const category = categories[id];
        $("#pricing-range-" + id).on("change input", (element) => {
            const selectedValue = element.target.value;
            $("#pricing-id-" + id).text(selectedValue);
            $("#pricing-info-" + id).text(category.info);

            const selectedConfig = category.config[selectedValue];

            const currency = selectedConfig.cost.currency;
            const monthlyPrice = selectedConfig.cost.value;
            $("#pricing-cost-" + id).text(currency + monthlyPrice);

            const hourlyPrice = (monthlyPrice / 24) / (5 / 0.18);
            $("#pricing-cost-hourly-" + id).text(currency + hourlyPrice);

            const specifications = selectedConfig.spec;
            for(let spec in specifications) {
                $("#pricing-spec-" + spec + "-" + id).text(
                    specifications[spec].value + " " + specifications[spec].unit
                );
                if(spec === "storage") {
                    $("#pricing-spec-storage-type-" + id).text(specifications[spec].type);
                }
            }
        });
        $("#pricing-range-" + id)
            .val(Object.keys(category.config)[0])
            .attr("max", Object.keys(category.config).length + "")
            .change();
    }
};

const registerCategoryProductPurchase = (categories, purchaseUrl) => {
    for(let category in categories) {
        $(`#pricing-spec-purchase-${ category }`).click(() => {
            
            const productId = $(`#pricing-id-${ category }`).text();
            const requestData = { product_id: productId, product_category: category };
            alert(JSON.stringify(requestData));

            /*
            In the future just use GET AJAX request to obtain the products
            */
            
        });
    }
}
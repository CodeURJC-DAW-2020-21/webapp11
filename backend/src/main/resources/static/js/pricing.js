$(document).ready(() => {

    // - the key represents the category
    // - the value is an array with the product id numbers
    let products = {};

    // Load all the products to the products object:
    $(".upper-item").each(function() {
        const element = $(this);
        const category = element.data("daw-category");
        if(!(category in products)) products[category] = [];
        const id = element.attr('id').split("-")[2];
        products[category].push(id);
    });

    // If no products to display redirect to error
    if(products.length === 0) {
        window.location.replace("/error");
    }

    // Category button template
    const buttonTemplate =
    `
    <button id="category-{category}"
            class="product-category nav-link mx-1 bg-transparent text-white {active}"
            data-bs-toggle="tab"
            data-bs-target="#nav-home" type="button" role="tab" aria-controls="nav-home"
            aria-selected="true"
            style="width: 220px;">
        <i class="{icon} text-light"></i>
        <span>{display_category} Server</span>
    </button>
    `;

    const icons = [
        "bi bi-share",
        "bi bi-hdd",
        "bi bi-hdd-rack",
        "bi bi-hdd-fill",
        "bi bi-hdd-rack-fill",
        "bi-server"
    ];

    // Print category navigation buttons
    let counter = 0;
    for(let category in products) {
        const capitalize = (s) => { return s && s[0].toUpperCase() + s.slice(1); };
        const findIcon = (i) => { if(i > icons.length - 1) i = icons.length - 1; return icons[i]; };
        const shouldActive = () => {
            const categoryToLoad = $("#load-category");
            if(categoryToLoad.val() !== "") {
                return category === categoryToLoad.val();
            }
            return counter === 0;
        };
        const concreteButton = buttonTemplate
            .replaceAll("{category}", category)
            .replaceAll("{display_category}", capitalize(category))
            .replaceAll("{icon}", findIcon(counter))
            .replaceAll("{active}", shouldActive() ? "active" : "");
        $("#tabs-container").append(concreteButton);
        counter++;
    }

    const categoryToLoad = $("#load-category");
    const defaultCategory = categoryToLoad.val() === "" ? Object.keys(products)[0] : categoryToLoad.val();
    const defaultProductsAmount = products[defaultCategory].length;

    // Set the default slider side depending on category products amount
    const packageSwitcher = $("#package-switcher");
    packageSwitcher.attr("max", defaultProductsAmount);
    packageSwitcher.val("1");
    $("#selected-category").text(defaultCategory);

    const setProductVisible = (id, visible) => {
        if(visible) {
            $(`#upper-product-${id}`).removeClass("d-none");
            $(`#bottom-product-${id}`).removeClass("d-none");
        } else {
            $(`#upper-product-${id}`).addClass("d-none");
            $(`#bottom-product-${id}`).addClass("d-none");
        }
    }

    let currentCategory = defaultCategory;
    let currentElementId = products[currentCategory][0];

    // Register category switch
    for(let category in products) {
        const currentElement = $(`#category-${category}`);
        currentElement.on("click", () => {
            $(`#category-${currentCategory}`).removeClass("active");
            currentElement.addClass("active");
            setProductVisible(currentElementId, false);
            currentCategory = category;
            currentElementId = products[currentCategory][0];
            packageSwitcher.attr("max", products[currentCategory].length);
            packageSwitcher.val("1");
            setProductVisible(currentElementId, true);
            $("#selected-category").text(currentCategory);
        });
    }

    // Set first product element visible
    setProductVisible(currentElementId, true);

    // Switch package element change
    packageSwitcher.on("change input", (element) => {
        const selectedValue = element.target.value;
        const chosenProductIndex = selectedValue - 1;
        const chosenProductElement = products[currentCategory][chosenProductIndex];

        setProductVisible(currentElementId, false);
        currentElementId = chosenProductElement;
        setProductVisible(currentElementId, true);
    });

    let otd = { defined: false };
    let ad = { defined: false };

    // Sales and discounts message
    $("#discounts > span").each(function() {
        const input = $(this);
        const content = input.text();
        const pieces = content.split(":");
        const type = pieces[7];

        const price = Number(pieces[5]);
        const discount = Number(pieces[6]);
        const discountedPrice = Math.floor(((100 - discount) * price) / 100);

        if(type === "otd") {
            otd = {
                defined: true,
                category: pieces[0],
                ram: pieces[1] + " RAM",
                cores: pieces[2] + " CORES",
                storage: pieces[3] + " STORAGE",
                transfer: pieces[4] + " TRANSFER",
                price: "$" + pieces[5],
                discountedPrice: "$" + discountedPrice,
                discount: pieces[6]
            };
        }
        if(type === "ad") {
            ad = {
                defined: true,
                category: pieces[0],
                ram: pieces[1] + " RAM",
                cores: pieces[2] + " CORES",
                storage: pieces[3] + " STORAGE",
                transfer: pieces[4] + " TRANSFER",
                price: "$" + pieces[5],
                discountedPrice: "$" + discountedPrice,
                discount: pieces[6],
                amountToPurchase: pieces[8]
            };
        }
    });

    let otdMessage = "";

    if(otd.defined) {
        otdMessage += `
            <p class="m-1 px-3">
                <span class="badge badge-pill bg-success">SALE ACTIVE!</span>
                We offer a <b>one time <span class="text-success">${otd.discount}%
                </span> discount</b> on the <b>${otd.category}</b>
                servers category with the configuration: <b>${otd.ram}</b>, 
                <b>${otd.cores}</b>, <b>${otd.storage}</b>, <b>${otd.transfer}</b>
                before it was <b class="text-danger"><s>${otd.price}</s></b> priced,
                but for your first purchase it will be 
                <b class="text-success">${otd.discountedPrice}</b>
            </p>
        `;
    }

    let adMessage = "";

    if(ad.defined) {
        adMessage += `
            <p class="m-1 px-3">
                <span class="badge badge-pill bg-success">SALE ACTIVE!</span>
                We offer a <b class="text-success">${ad.discount}%</b> <b>accumulative 
                discount</b> on the <b>${ad.category}</b> servers category with 
                the configuration: <b>${ad.ram}</b>, <b>${ad.cores}</b>, <b>${ad.storage}</b>, 
                <b>${ad.transfer}</b> if you buy <b>${ad.amountToPurchase}</b> servers 
                all the next purchases on the same package will cost now 
                <b class="text-success">${ad.discountedPrice}</b> (previous cost
                <b class="text-danger"><s>${ad.price}</s></b>)
            </p>
        `;
    }

    let currentMessage = 2;
    const displayToggle = () => {
        currentMessage = currentMessage === 1 ? 2 : 1;
        let message = currentMessage === 1 ? otdMessage : adMessage;
        if(message === "") return;
        const messageTemplate =
            `
        <div id="flash" class="mx-5 my-3">
            <div class="alert text-center bg-transparent border border-1 mx-5 rounded alert-light border-light text-light fade show" role="alert">
                <div id="discount-message">${message}</div>
            </div>
        </div>
        `;
        $("#flash").replaceWith(messageTemplate);
    };

    const interval = setInterval(function() {
        displayToggle();
    }, 10000);
    for(let i = 0; i < 2; i++) {
        displayToggle();
    }

});



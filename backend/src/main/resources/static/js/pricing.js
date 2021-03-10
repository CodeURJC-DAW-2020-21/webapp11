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

});



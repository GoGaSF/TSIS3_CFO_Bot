const chatBox = document.getElementById("chatBox");
const chatForm = document.getElementById("chatForm");
const resetBtn = document.getElementById("resetBtn");
const exampleBtn = document.getElementById("exampleBtn");
const startupExampleBtn = document.getElementById("startupExampleBtn");
const growthExampleBtn = document.getElementById("growthExampleBtn");
const scaleExampleBtn = document.getElementById("scaleExampleBtn");
const sendBtn = document.getElementById("sendBtn");
const themeToggleBtn = document.getElementById("themeToggleBtn");
const typingIndicator = document.getElementById("typingIndicator");
const explainBtn = document.getElementById("explainBtn");
const cheaperBtn = document.getElementById("cheaperBtn");
const resendBtn = document.getElementById("resendBtn");
const showSummaryBtn = document.getElementById("showSummaryBtn");
const logoutBtn = document.getElementById("logoutBtn");
const userBadge = document.getElementById("userBadge");
const exportCsvBtn = document.getElementById("exportCsvBtn");
const pricingBtn = document.getElementById("pricingBtn");
const pricingModal = document.getElementById("pricingModal");
const closePricingModalBtn = document.getElementById("closePricingModalBtn");
const pricingContent = document.getElementById("pricingContent");
const compareScenario1 = document.getElementById("compareScenario1");
const compareScenario2 = document.getElementById("compareScenario2");
const compareBtn = document.getElementById("compareBtn");
const comparisonResult = document.getElementById("comparisonResult");

let lastCalculation = null;
let lastRequest = null;
let lastRecommendations = [];
let cachedScenarios = [];

function formatMoney(value) {
    return "$" + Number(value).toFixed(2);
}

function addMessage(sender, text, extraClass = "", author = "") {
    const div = document.createElement("div");
    div.className = "message " + sender + (extraClass ? " " + extraClass : "");

    const meta = document.createElement("div");
    meta.className = "message-meta";
    meta.textContent = author ? author : (sender === "bot" ? "CFO Bot" : "User");
    div.appendChild(meta);

    const pre = document.createElement("pre");
    pre.textContent = text;

    div.appendChild(pre);
    chatBox.appendChild(div);
    chatBox.scrollTop = chatBox.scrollHeight;
}

function clearChat() {
    chatBox.innerHTML = "";
    addMessage(
        "bot",
        "Hello! I am CFO Bot.\nI can estimate your monthly cloud cost for compute, storage, bandwidth, and database services.\n\nYou can fill the form below or use one of the quick scenarios on the left."
    );
}

function setFormValues(computeTier, computeHours, storageGb, bandwidthGb, databaseTier) {
    document.getElementById("computeTier").value = computeTier;
    document.getElementById("computeHours").value = computeHours;
    document.getElementById("storageGb").value = storageGb;
    document.getElementById("bandwidthGb").value = bandwidthGb;
    document.getElementById("databaseTier").value = databaseTier;
}

function fillDemoExample() {
    setFormValues("medium", "300", "500", "700", "standard");
}

function fillStartupExample() {
    setFormValues("small", "120", "100", "150", "basic");
}

function fillGrowthExample() {
    setFormValues("medium", "300", "500", "700", "standard");
}

function fillScaleExample() {
    setFormValues("large", "650", "1500", "2200", "premium");
}

function resetSummary() {
    document.getElementById("computeValue").textContent = "$0.00";
    document.getElementById("storageValue").textContent = "$0.00";
    document.getElementById("bandwidthValue").textContent = "$0.00";
    document.getElementById("databaseValue").textContent = "$0.00";
    document.getElementById("totalValue").textContent = "$0.00";
    document.getElementById("summarySubtext").textContent = "No calculation yet";
    document.getElementById("recommendationText").textContent = "Run a calculation to see optimization suggestions.";
    document.getElementById("driverText").textContent = "Not available yet.";
    document.getElementById("selectedComputeTier").textContent = "-";
    document.getElementById("selectedDatabaseTier").textContent = "-";
    document.getElementById("selectedHours").textContent = "-";
    document.getElementById("selectedStorage").textContent = "-";
    document.getElementById("selectedBandwidth").textContent = "-";
}

function updateSummary(data, request) {
    document.getElementById("computeValue").textContent = formatMoney(data.computeCost);
    document.getElementById("storageValue").textContent = formatMoney(data.storageCost);
    document.getElementById("bandwidthValue").textContent = formatMoney(data.bandwidthCost);
    document.getElementById("databaseValue").textContent = formatMoney(data.databaseCost);
    document.getElementById("totalValue").textContent = formatMoney(data.totalCost);
    document.getElementById("summarySubtext").textContent =
        request.computeTier + " compute • " + request.databaseTier + " database";

    document.getElementById("selectedComputeTier").textContent = request.computeTier;
    document.getElementById("selectedDatabaseTier").textContent = request.databaseTier;
    document.getElementById("selectedHours").textContent = request.computeHours;
    document.getElementById("selectedStorage").textContent = request.storageGb + " GB";
    document.getElementById("selectedBandwidth").textContent = request.bandwidthGb + " GB";

    const items = [
        { name: "Compute", value: data.computeCost },
        { name: "Storage", value: data.storageCost },
        { name: "Bandwidth", value: data.bandwidthCost },
        { name: "Database", value: data.databaseCost }
    ];

    let maxItem = items[0];
    for (let i = 1; i < items.length; i++) {
        if (items[i].value > maxItem.value) {
            maxItem = items[i];
        }
    }

    document.getElementById("driverText").textContent =
        maxItem.name + " is currently the largest cost driver at " + formatMoney(maxItem.value) + ".";
}

function buildUserMessage(request) {
    return (
        "Compute tier: " + request.computeTier + "\n" +
        "Compute hours: " + request.computeHours + "\n" +
        "Storage GB: " + request.storageGb + "\n" +
        "Bandwidth GB: " + request.bandwidthGb + "\n" +
        "Database tier: " + request.databaseTier
    );
}

function getRequestFromForm() {
    return {
        computeTier: document.getElementById("computeTier").value,
        computeHours: Number(document.getElementById("computeHours").value),
        storageGb: Number(document.getElementById("storageGb").value),
        bandwidthGb: Number(document.getElementById("bandwidthGb").value),
        databaseTier: document.getElementById("databaseTier").value
    };
}

function validateBasicFields(request) {
    if (isNaN(request.computeHours) || isNaN(request.storageGb) || isNaN(request.bandwidthGb)) {
        addMessage("bot", "Please fill all numeric fields before sending the request.", "error");
        return false;
    }
    return true;
}
function exportCurrentCalculationToCsv() {
    if (!lastCalculation || !lastRequest) {
        addMessage("bot", "Run a calculation first before exporting CSV.");
        return;
    }

    const rows = [
        ["Field", "Value"],
        ["Compute Tier", lastRequest.computeTier],
        ["Compute Hours", lastRequest.computeHours],
        ["Storage GB", lastRequest.storageGb],
        ["Bandwidth GB", lastRequest.bandwidthGb],
        ["Database Tier", lastRequest.databaseTier],
        ["Compute Cost", lastCalculation.computeCost],
        ["Storage Cost", lastCalculation.storageCost],
        ["Bandwidth Cost", lastCalculation.bandwidthCost],
        ["Database Cost", lastCalculation.databaseCost],
        ["Total Cost", lastCalculation.totalCost]
    ];

    const csvContent = rows.map(row => row.join(",")).join("\n");
    const blob = new Blob([csvContent], { type: "text/csv;charset=utf-8;" });
    const url = URL.createObjectURL(blob);

    const link = document.createElement("a");
    link.href = url;
    link.download = "cfobot-estimate.csv";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);

    URL.revokeObjectURL(url);

    addMessage("bot", "CSV export completed successfully.");
}

async function fetchRecommendations(request) {
    const response = await fetch("/api/cfo/recommendation", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(request)
    });

    if (!response.ok) {
        throw new Error("Failed to fetch recommendations");
    }

    return await response.json();
}

async function openPricingModal() {
    pricingModal.classList.remove("hidden");
    pricingContent.innerHTML = '<div class="mini-empty">Loading pricing model...</div>';

    try {
        const response = await fetch("/api/cfo/pricing");
        if (!response.ok) {
            throw new Error("Failed to fetch pricing model");
        }

        const data = await response.json();
        pricingContent.innerHTML = "";

        Object.entries(data).forEach(([key, value]) => {
            const row = document.createElement("div");
            row.className = "pricing-row";

            const left = document.createElement("span");
            left.textContent = key.replaceAll("_", " ");

            const right = document.createElement("strong");
            right.textContent = "$" + value;

            row.appendChild(left);
            row.appendChild(right);
            pricingContent.appendChild(row);
        });
    } catch (error) {
        pricingContent.innerHTML = '<div class="mini-empty">Unable to load pricing model.</div>';
        console.error(error);
    }
}

async function refreshHistory() {
    const historyList = document.getElementById("historyList");

    try {
        const response = await fetch("/api/cfo/history");
        if (!response.ok) {
            throw new Error("Failed to fetch history");
        }

        const history = await response.json();
        historyList.innerHTML = "";

        if (!history.length) {
            historyList.innerHTML = '<div class="mini-empty">No recent calculations yet.</div>';
            return;
        }

        history.forEach((item, index) => {
            const div = document.createElement("div");
            div.className = "mini-item";

            const title = document.createElement("strong");
            title.textContent =
                (index + 1) + ". " +
                item.request.computeTier + " / " +
                item.request.databaseTier + " • " +
                formatMoney(item.response.totalCost);

            const desc = document.createElement("p");
            desc.textContent =
                "Hours: " + item.request.computeHours +
                ", Storage: " + item.request.storageGb + " GB, Bandwidth: " +
                item.request.bandwidthGb + " GB";

            const btn = document.createElement("button");
            btn.type = "button";
            btn.textContent = "Load";
            btn.addEventListener("click", function () {
                setFormValues(
                    item.request.computeTier,
                    item.request.computeHours,
                    item.request.storageGb,
                    item.request.bandwidthGb,
                    item.request.databaseTier
                );
            });

            div.appendChild(title);
            div.appendChild(desc);
            div.appendChild(btn);
            historyList.appendChild(div);
        });
    } catch (error) {
        historyList.innerHTML = '<div class="mini-empty">Unable to load history.</div>';
        console.error(error);
    }
}

async function refreshScenarios() {
    const scenariosList = document.getElementById("scenariosList");

    try {
        const response = await fetch("/api/cfo/scenarios");
        if (!response.ok) {
            throw new Error("Failed to fetch scenarios");
        }

        const scenarios = await response.json();
        cachedScenarios = scenarios;
        scenariosList.innerHTML = "";

        compareScenario1.innerHTML = "";
        compareScenario2.innerHTML = "";

        scenarios.forEach((scenario, index) => {
            const div = document.createElement("div");
            div.className = "mini-item";

            const title = document.createElement("strong");
            title.textContent = scenario.name + " • " + formatMoney(scenario.result.totalCost);

            const desc = document.createElement("p");
            desc.textContent = scenario.description;

            const btn = document.createElement("button");
            btn.type = "button";
            btn.textContent = "Load scenario";
            btn.addEventListener("click", function () {
                setFormValues(
                    scenario.request.computeTier,
                    scenario.request.computeHours,
                    scenario.request.storageGb,
                    scenario.request.bandwidthGb,
                    scenario.request.databaseTier
                );
            });

            div.appendChild(title);
            div.appendChild(desc);
            div.appendChild(btn);
            scenariosList.appendChild(div);

            const option1 = document.createElement("option");
            option1.value = index;
            option1.textContent = scenario.name + " • " + formatMoney(scenario.result.totalCost);
            compareScenario1.appendChild(option1);

            const option2 = document.createElement("option");
            option2.value = index;
            option2.textContent = scenario.name + " • " + formatMoney(scenario.result.totalCost);
            compareScenario2.appendChild(option2);
        });

        if (cachedScenarios.length > 1) {
            compareScenario1.value = "0";
            compareScenario2.value = "1";
        }
    } catch (error) {
        scenariosList.innerHTML = '<div class="mini-empty">Unable to load scenarios.</div>';
        console.error(error);
    }
}

function compareSelectedScenarios() {
    if (cachedScenarios.length < 2) {
        comparisonResult.textContent = "Not enough scenarios available for comparison.";
        return;
    }

    const firstIndex = Number(compareScenario1.value);
    const secondIndex = Number(compareScenario2.value);

    if (firstIndex === secondIndex) {
        comparisonResult.textContent = "Please choose two different scenarios.";
        return;
    }

    const first = cachedScenarios[firstIndex];
    const second = cachedScenarios[secondIndex];

    const firstTotal = Number(first.result.totalCost);
    const secondTotal = Number(second.result.totalCost);

    const cheaper = firstTotal < secondTotal ? first : second;
    const expensive = firstTotal > secondTotal ? first : second;
    const diff = Math.abs(firstTotal - secondTotal).toFixed(2);

    comparisonResult.textContent =
        cheaper.name + " is cheaper than " + expensive.name +
        " by $" + diff +
        ". Totals: " +
        first.name + " = " + formatMoney(firstTotal) + ", " +
        second.name + " = " + formatMoney(secondTotal) + ".";
}

async function sendCalculation(request, addUserBubble = true) {
    if (!validateBasicFields(request)) {
        return;
    }

    lastRequest = request;

    if (addUserBubble) {
        addMessage("user", buildUserMessage(request));
    }

    sendBtn.disabled = true;
    sendBtn.textContent = "Calculating...";
    typingIndicator.classList.remove("hidden");

    try {
        const calculateResponse = await fetch("/api/cfo/calculate", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(request)
        });

        if (!calculateResponse.ok) {
            let errorText = "Calculation request failed";
            try {
                errorText = await calculateResponse.text();
            } catch (e) {
                console.error(e);
            }
            throw new Error(errorText);
        }

        const calculationData = await calculateResponse.json();
        lastCalculation = calculationData;
        updateSummary(calculationData, request);

        const recommendationData = await fetchRecommendations(request);
        lastRecommendations = recommendationData.recommendations || [];

        document.getElementById("recommendationText").textContent =
            lastRecommendations.length ? lastRecommendations[0] : "No recommendations available.";

        const chatResponse = await fetch("/api/cfo/chat", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(request)
        });

        if (!chatResponse.ok) {
            let errorText = "Chat request failed";
            try {
                errorText = await chatResponse.text();
            } catch (e) {
                console.error(e);
            }
            throw new Error(errorText);
        }

        const chatData = await chatResponse.json();
        addMessage("bot", chatData.message);

        await refreshHistory();
    } catch (error) {
        addMessage(
            "bot",
            "Error while calculating cloud cost.\nPlease check your input values or backend logic.",
            "error"
        );
        console.error(error);
    } finally {
        typingIndicator.classList.add("hidden");
        sendBtn.disabled = false;
        sendBtn.textContent = "Send to CFO Bot";
    }
}

chatForm.addEventListener("submit", async function (e) {
    e.preventDefault();
    const request = getRequestFromForm();
    await sendCalculation(request, true);
});

resetBtn.addEventListener("click", function () {
    clearChat();
    chatForm.reset();
    setFormValues("medium", "", "", "", "standard");
    lastCalculation = null;
    lastRequest = null;
    lastRecommendations = [];
    resetSummary();
});

exampleBtn.addEventListener("click", fillDemoExample);
startupExampleBtn.addEventListener("click", fillStartupExample);
growthExampleBtn.addEventListener("click", fillGrowthExample);
scaleExampleBtn.addEventListener("click", fillScaleExample);

exportCsvBtn.addEventListener("click", exportCurrentCalculationToCsv);

pricingBtn.addEventListener("click", openPricingModal);

closePricingModalBtn.addEventListener("click", function () {
    pricingModal.classList.add("hidden");
});

pricingModal.addEventListener("click", function (e) {
    if (e.target === pricingModal) {
        pricingModal.classList.add("hidden");
    }
});

compareBtn.addEventListener("click", compareSelectedScenarios);

themeToggleBtn.addEventListener("click", function () {
    document.body.classList.toggle("dark");
    themeToggleBtn.textContent = document.body.classList.contains("dark") ? "Light mode" : "Dark mode";
});

showSummaryBtn.addEventListener("click", function () {
    const summaryPanel = document.getElementById("summaryPanel");
    summaryPanel.scrollIntoView({ behavior: "smooth", block: "start" });
});

explainBtn.addEventListener("click", function () {
    if (!lastCalculation || !lastRequest) {
        addMessage("bot", "Run a calculation first, and then I can explain the estimate.");
        return;
    }

    const text =
        "Estimate explanation:\n\n" +
        "Compute cost is based on " + lastRequest.computeHours + " hours using the " + lastRequest.computeTier + " tier.\n" +
        "Storage cost is calculated from " + lastRequest.storageGb + " GB.\n" +
        "Bandwidth cost depends on " + lastRequest.bandwidthGb + " GB of outgoing traffic.\n" +
        "Database cost comes from the selected " + lastRequest.databaseTier + " tier.\n\n" +
        "The final monthly total is " + formatMoney(lastCalculation.totalCost) + ".";

    addMessage("bot", text);
});

cheaperBtn.addEventListener("click", function () {
    if (!lastCalculation || !lastRequest) {
        addMessage("bot", "Run a calculation first, and then I can suggest a cheaper option.");
        return;
    }

    if (!lastRecommendations.length) {
        addMessage("bot", "No cheaper option is currently available.");
        return;
    }

    let text = "Optimization suggestions:\n\n";
    lastRecommendations.forEach((item) => {
        text += "- " + item + "\n";
    });

    addMessage("bot", text);
});

resendBtn.addEventListener("click", async function () {
    const request = getRequestFromForm();
    await sendCalculation(request, false);
});

async function checkSession() {
    try {
        const response = await fetch("/api/auth/me");
        const data = await response.json();

        if (!data.success) {
            window.location.href = "/login.html";
            return;
        }

        userBadge.textContent = data.name + " • " + data.email;
    } catch (error) {
        window.location.href = "/login.html";
        console.error(error);
    }
}

logoutBtn.addEventListener("click", async function () {
    try {
        await fetch("/api/auth/logout", {
            method: "POST"
        });
    } catch (error) {
        console.error(error);
    } finally {
        window.location.href = "/login.html";
    }
});

async function initApp() {
    await checkSession();
    clearChat();
    resetSummary();
    await refreshScenarios();
    await refreshHistory();
}

initApp();
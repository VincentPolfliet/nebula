package dev.vinpol.nebula.dragonship.web.contracts;

import dev.vinpol.nebula.dragonship.web.HtmlPage;
import dev.vinpol.nebula.dragonship.web.HtmlPage;
import dev.vinpol.spacetraders.sdk.api.ContractsApi;
import dev.vinpol.spacetraders.sdk.models.AcceptContract200Response;
import dev.vinpol.spacetraders.sdk.models.AcceptContract200ResponseData;
import dev.vinpol.spacetraders.sdk.models.Contract;
import dev.vinpol.spacetraders.sdk.models.GetContracts200Response;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContractsController {

    private final ContractsApi contractsApi;

    public ContractsController(ContractsApi contractsApi) {
        this.contractsApi = contractsApi;
    }

    @GetMapping(value = "/contracts")
    public String getContracts(@RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "total", defaultValue = "10") int total,
                               Model model) {

        HtmlPage contentPage = new HtmlPage();
        contentPage.setTitle("Contracts");

        model.addAttribute("page", contentPage);

        GetContracts200Response response = contractsApi.getContracts(page, total);
        model.addAttribute("contracts", response.getData());

        return "contracts/index";
    }

    @PostMapping(value = "/contracts/{contractId}")
    public String postContract(@PathVariable("contractId") String contractId, Model model) {
        AcceptContract200Response acceptContract200Response = contractsApi.acceptContract(contractId);
        AcceptContract200ResponseData data = acceptContract200Response.getData();

        Contract contract = data.getContract();
        model.addAttribute("contract", contract);
        return "contracts/contract-row";
    }
}

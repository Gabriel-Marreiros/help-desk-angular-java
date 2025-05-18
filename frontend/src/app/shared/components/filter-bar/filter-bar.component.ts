import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-filter-bar',
  templateUrl: './filter-bar.component.html',
  styleUrls: ['./filter-bar.component.scss']
})
export class FilterBarComponent implements OnInit {
  searchFilter!: string;

  constructor(
    private currentRoute: ActivatedRoute,
    private router: Router
  ){}

  ngOnInit(): void {
    this.currentRoute.queryParams.subscribe(({search}) => {
      this.searchFilter = search;
    })
  }

  handleClearSearchFilter(): void {
    this.searchFilter = '';
    this.router.navigate([], {
      queryParams: {search: null},
      queryParamsHandling: 'merge'
    });
  }

  showClearSearchFilterButton(): boolean {
    let hasSearchParams: boolean = false;

    this.currentRoute.queryParams.subscribe((params) => {
      hasSearchParams = params["search"];
    });

    return hasSearchParams;
  }

  applySearchFilter(searchFilter: string): void {
    this.router.navigate([], {
      queryParams: {
        search: searchFilter
      },
      queryParamsHandling: 'merge'
    });
  }

  handleSelectFilter(): void {

  }

}

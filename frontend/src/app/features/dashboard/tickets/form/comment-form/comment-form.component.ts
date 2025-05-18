import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { AngularEditorConfig } from '@kolkov/angular-editor';

@Component({
  selector: 'app-comment-form',
  templateUrl: './comment-form.component.html',
  styleUrls: ['./comment-form.component.scss']
})
export class CommentFormComponent {
  @Input()
  commentForm!: FormGroup

  @Output()
  onSubmit: EventEmitter<any> = new EventEmitter<any>();

  @Output()
  onCancel: EventEmitter<void> = new EventEmitter<void>();

  constructor(){}

  editorConfig: AngularEditorConfig = {
    editable: true,
    spellcheck: true,
    height: 'auto',
    minHeight: '200px',
    maxHeight: 'auto',
    width: 'auto',
    minWidth: '0',
    translate: 'yes',
    enableToolbar: true,
    showToolbar: true,
    placeholder: 'Digite o comentário...',
    defaultParagraphSeparator: '',
    defaultFontName: '',
    defaultFontSize: '',
    fonts: [
      {class: 'arial', name: 'Arial'},
      {class: 'times-new-roman', name: 'Times New Roman'},
      {class: 'calibri', name: 'Calibri'},
      {class: 'comic-sans-ms', name: 'Comic Sans MS'}
    ],
    // customClasses: [
    //   {
    //     name: 'quote',
    //     class: 'quote',
    //   },
    //   {
    //     name: 'redText',
    //     class: 'redText'
    //   },
    //   {
    //     name: 'titleText',
    //     class: 'titleText',
    //     tag: 'h1',
    //   },
    // ],

    // uploadUrl: 'v1/image',
    // upload: (file: File) => new Observable(),
    uploadWithCredentials: false,
    sanitize: true,
    toolbarPosition: 'top',
    toolbarHiddenButtons: [
      ["justifyLeft", "justifyCenter", "justifyRight", "justifyFull", "indent", "outdent", "toggleEditorMode", "insertVideo", "insertImage" ]
    ]
  };
}
